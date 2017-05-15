package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.image.HeadImageUploadRequest;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.net.request.my.UpdatePersonInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.contact.MyQRCodeActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.util.AppUtils;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.HttpCallBackListener;
import huanxing_print.com.cn.printhome.util.HttpUtil;
import huanxing_print.com.cn.printhome.util.MyDataCleanManager;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;


/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class MyActivity extends BaseActivity implements View.OnClickListener {

    //请求相机
    private  final int REQUEST_CAPTURE = 100;
    //请求相册
    private  final int REQUEST_PICK = 101;
    //请求截图
    private  final int REQUEST_CROP_PHOTO = 102;
    private  final int NAME_CODE = 103;
    private  final int PHONE_CODE = 104;
    private  final int WEIXIN_CODE = 105;
    private String oriPath = ConFig.IMG_CACHE_PATH + File.separator + "headImg.jpg";
    private ImageView iv_user_head;
    private LinearLayout ll_back;

    private TextView tv_uniqueid,tv_userInfo_nickname,tv_phone,tv_version,tv_weixin,tv_cache;
    private String cropImagePath;
    private String uniqueId,nickName,wdixinName,phone,weixin,uniqueModifyFlag;
    private Bitmap bitMap;
    private String version;
    private String ApkUrl;
    private String dataSize;//缓存大小

    private String openid;
    //微信登录
    private IWXAPI api;
    private ReceiveBroadCast receiveBroadCast;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_user_my);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
    }
    @Override
    public void onResume() {
        super.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("authlogin");
        registerReceiver(receiveBroadCast, filter);
    }
    /**
     * 获取用户信息
     */
    private void initData() {
        uniqueId = baseApplication.getUniqueId();
        nickName = baseApplication.getNickName();
        wdixinName= baseApplication.getWeixinName();
        phone = baseApplication.getPhone();
        weixin = baseApplication.getWechatId();
        uniqueModifyFlag = baseApplication.getUniqueModifyFlag();
        BitmapUtils.displayImage(getSelfActivity(), baseApplication.getHeadImg(),
                R.drawable.iv_head, iv_user_head);

        if (!ObjectUtils.isNull(uniqueId)) {
            tv_uniqueid.setText(uniqueId);
        }
        if (!ObjectUtils.isNull(nickName)) {
            tv_userInfo_nickname.setText(nickName);
        }
        if (!ObjectUtils.isNull(phone)) {
            tv_phone.setText(phone);
        }
        if (!ObjectUtils.isNull(wdixinName)) {
            tv_weixin.setText(wdixinName);
        }
        tv_version.setText("当前V"+version+"版本");

        try {
            dataSize = MyDataCleanManager.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ObjectUtils.isNull(dataSize)) {
            tv_cache.setText(dataSize);
        }

    }


    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private void initView() {
        version = AppUtils.getVersionName(getSelfActivity());
        ApkUrl = baseApplication.getApkUrl();
        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_uniqueid = (TextView) findViewById(R.id.tv_uniqueid);
        tv_userInfo_nickname = (TextView) findViewById(R.id.tv_userInfo_nickname);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_version= (TextView) findViewById(R.id.tv_version);
        tv_weixin= (TextView) findViewById(R.id.tv_weixin);
        tv_cache= (TextView) findViewById(R.id.tv_cache);
    }

    private void setListener() {
        iv_user_head.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        findViewById(R.id.ll_userInfo_code).setOnClickListener(this);
        findViewById(R.id.ll_uniqueid).setOnClickListener(this);
        findViewById(R.id.ll_userInfo_name).setOnClickListener(this);
        findViewById(R.id.ll_userInfo_phone).setOnClickListener(this);
        findViewById(R.id.ll_userInfo_weixin).setOnClickListener(this);
        findViewById(R.id.ll_cache).setOnClickListener(this);
        findViewById(R.id.ll_loginout).setOnClickListener(this);
        findViewById(R.id.rl_set_version).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.iv_user_head:
                DialogUtils.showPicChooseDialog(getSelfActivity(), new DialogUtils.PicChooseDialogCallBack() {

                    @Override
                    public void photo() {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, REQUEST_CAPTURE);
                    }

                    @Override
                    public void camera() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(oriPath)));
                        startActivityForResult(intent, REQUEST_PICK);

                    }
                }).show();
                break;
            case R.id.ll_userInfo_code:
                Intent codeIntent = new Intent(getSelfActivity(), MyQRCodeActivity.class);
                startActivity(codeIntent);
                break;
            case R.id.ll_uniqueid:
                if(!ObjectUtils.isNull(uniqueModifyFlag)&&"true".equals(uniqueModifyFlag)) {
                    Intent uniqueIdIntent = new Intent(getSelfActivity(), MyModifyUniqueIdActivty.class);
                    uniqueIdIntent.putExtra("uniqueId", uniqueId);
                    startActivity(uniqueIdIntent);
                }
                break;
            case R.id.ll_userInfo_name:
                Intent nameIntent = new Intent(getSelfActivity(), MyModifyNameActivty.class);
                nameIntent.putExtra("nickName", nickName);
                startActivityForResult(nameIntent, NAME_CODE);
                break;
            case R.id.ll_userInfo_phone:
                Intent phoneIntent = new Intent(getSelfActivity(), MyModifyPhoneActivty.class);
                phoneIntent.putExtra("phone", phone);
                startActivityForResult(phoneIntent, PHONE_CODE);
                break;
            case R.id.ll_userInfo_weixin:
//                Intent weixinIntent = new Intent(getSelfActivity(), MyModifyWeixinActivty.class);
//                weixinIntent.putExtra("weixin", weixin);
//                startActivityForResult(weixinIntent, PHONE_CODE);

                if (CommonUtils.isWeixinAvilible(getSelfActivity())) {
                    weChatAuth();
                } else {
                    ToastUtil.doToast(getSelfActivity(), "您还没有安装微信，请先安装微信客户端");
                }
                break;
            case R.id.rl_set_version://版本更新
                //startActivity(new Intent(getSelfActivity(), OperatingInstructionsActivity.class));
                if (baseApplication.isNewApp()){
                    toast("当前版本为最新版本");
                }else{
                    DialogUtils.showTipsDialog(getSelfActivity(), getResources().getString(R.string.dlg_content_update_version),
                            new DialogUtils.TipsDialogCallBack() {
                                @Override
                                public void ok() {
                                    if (!ObjectUtils.isNull(ApkUrl)) {
                                        Uri uri = Uri.parse(ApkUrl);
                                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(it);
                                    }

                                }
                            }).show();
                }
                break;
            case R.id.ll_cache://清除缓存
                DialogUtils.showTipsDialog(getSelfActivity(), getResources().getString(R.string.dlg_content_clear_cache),
                        new DialogUtils.TipsDialogCallBack() {
                            @Override
                            public void ok() {
                                MyDataCleanManager.clearAllCache(getSelfActivity());
                                tv_cache.setText("0KB");
                            }
                        }).show();

                break;

            case R.id.ll_loginout:// 退出当前账号
                DialogUtils.showTipsDialog(getSelfActivity(), getResources().getString(R.string.dlg_content_loginout),
                        new DialogUtils.TipsDialogCallBack() {
                            @Override
                            public void ok() {
                                DialogUtils.showProgressDialog(getSelfActivity(), "正在退出登录").show();
                                LoginRequset.loginOut(getSelfActivity(),
                                        baseApplication.getLoginToken(), loginoutcallback);

                            }
                        }).show();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE:
                if (null != intent) {
                    gotoClipActivity(intent.getData());
                }
                break;
            case REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    File temp = new File(oriPath);
                    gotoClipActivity(Uri.fromFile(temp));
                }
                break;
//            case REQUEST_CROP_PHOTO:
//                if (null != intent) {
//                    setPicToView(intent);
//                }
//                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);

                    BitmapUtils.displayImage(this, cropImagePath, R.drawable.iv_head, iv_user_head);

                    bitMap = BitmapFactory.decodeFile(cropImagePath);
                    setPicToView(bitMap);
                    //EventBus.getDefault().post(bitMap, "head");
                }
                break;
            case NAME_CODE:
                if (null != intent) {
                    nickName = intent.getStringExtra("nickName");
                    if (!ObjectUtils.isNull(nickName)) {
                        tv_userInfo_nickname.setText(nickName);
                    }

                }
                break;
            case PHONE_CODE:
                if (null != intent) {
                    phone = intent.getStringExtra("phone");
                    if (!ObjectUtils.isNull(phone)) {
                        tv_phone.setText(phone);
                    }

                }
                break;
            case WEIXIN_CODE:
                if (null != intent) {
                    uniqueId = intent.getStringExtra("uniqueId");
                    if (!ObjectUtils.isNull(uniqueId)) {
                        tv_uniqueid.setText(uniqueId);
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipeImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    private void setPicToView(Bitmap bitMap) {
        //Bundle extras = picdata.getExtras();
        if (null != bitMap) {
            //Bitmap photo = extras.getParcelable("data");
            //Bitmap photo =filterColor(phmp);
            iv_user_head.setImageBitmap(bitMap);
            String filePath = FileUtils.savePic(getSelfActivity(), "headImg.jpg", bitMap);
            if (!ObjectUtils.isNull(filePath)) {
                File file = new File(filePath);
                //file转化成二进制
                byte[] buffer = null;
                FileInputStream in ;
                int length = 0;
                try {
                    in = new FileInputStream(file);
                    buffer = new byte[(int) file.length() + 100];
                    length = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);

                DialogUtils.showProgressDialog(getSelfActivity(), "文件上传中").show();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("fileContent", data);
                params.put("fileName", filePath);
                params.put("fileType", ".jpg");
                HeadImageUploadRequest.upload(getSelfActivity(),  params,
                        new HeadImageUploadCallback() {

                            @Override
                            public void fail(String msg) {
                                toast(msg);
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void connectFail() {
                                toastConnectFail();
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void success(String msg, HeadImageBean bean) {
                                //Logger.d("PersonInfoActivity ---------HeadImage--------:" + bean.getImgUrl() );
                                baseApplication.setHeadImg(bean.getImgUrl());
                                //DialogUtils.closeProgressDialog();
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("faceUrl", bean.getImgUrl());
                                UpdatePersonInfoRequest.update(getSelfActivity(),  baseApplication.getLoginToken(),params, callback);
                            }
                        });
            }

        }
    }

    private NullCallback callback = new NullCallback() {

        @Override
        public void fail(String msg) {
            toast(msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            EventBus.getDefault().post(bitMap, "head");
            BitmapUtils.displayImage(getSelfActivity(), baseApplication.getHeadImg(), R.drawable.iv_head, iv_user_head);
            toast("头像更新成功");
        }
    };


    private NullCallback loginoutcallback = new NullCallback() {

        @Override
        public void fail(String msg) {
            toast(msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            clearUserData();// 清空数据
            EMClient.getInstance().logout(true);//环信退出
            ActivityHelper.getInstance().finishAllActivity();
            activityExitAnim();
            jumpActivityNoAnim(LoginActivity.class, false);
        }
    };

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private void weChatAuth() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(getSelfActivity(), baseApplication.WX_APPID, true);
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login_duzun";
        api.sendReq(req);
    }

    public void getAccessToken() {
        SharedPreferences WxSp = getSelfActivity().getApplicationContext()
                .getSharedPreferences(ConFig.spName, Context.MODE_PRIVATE);
        String code = WxSp.getString(ConFig.CODE, "");
        final SharedPreferences.Editor WxSpEditor = WxSp.edit();
        Log.d(TAG, "-----获取到的code----" + code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + baseApplication.WX_APPID
                + "&secret="
                + baseApplication.WX_APPSecret
                + "&code="
                + code
                + "&grant_type=authorization_code";
        Log.d(TAG, "--------即将获取到的access_token的地址--------");
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {

                //解析以及存储获取到的信息
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Log.d(TAG, "-----获取到的json数据1-----" + jsonObject.toString());
                    String access_token = jsonObject.getString("access_token");
                    //Log.d(TAG, "--------获取到的access_token的地址--------" + access_token);
                    openid = jsonObject.getString("openid");
                    String refresh_token = jsonObject.getString("refresh_token");
                    if (!ObjectUtils.isNull(access_token)) {
                        WxSpEditor.putString(ConFig.ACCESS_TOKEN, access_token);
                        WxSpEditor.apply();
                    }
                    if (!refresh_token.equals("")) {
                        WxSpEditor.putString(ConFig.REFRESH_TOKEN, refresh_token);
                        WxSpEditor.apply();
                    }
                    if (!openid.equals("")) {
                        WxSpEditor.putString(ConFig.WXOPENID, openid);
                        WxSpEditor.apply();
                        getPersonMessage(access_token, openid);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getSelfActivity(), "通过code获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPersonMessage(String access_token, final String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        HttpUtil.sendHttpRequest(url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "------获取到的个人信息------" + jsonObject.toString());
                    String nickName = jsonObject.getString("nickname");
                    baseApplication.setWeixinName(nickName);
                    String sex = jsonObject.getString("sex");
                    String province = jsonObject.getString("province");
                    String city = jsonObject.getString("city");
                    String country = jsonObject.getString("country");
                    String headImgurl = jsonObject.getString("headimgurl");
                    String privilege = jsonObject.getString("privilege");
                    String unionId = jsonObject.getString("unionid");
                    MyInfoRequest.bindWechat(getSelfActivity(),baseApplication.getLoginToken(),
                            city, country, headImgurl, nickName, openid,
                            privilege, sex, unionId, weiXinCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getSelfActivity(), "通过openid获取数据没有成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //登录接口返回
    private NullCallback weiXinCallback = new NullCallback() {


        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            toast("绑定成功");
        }
    };

    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getAccessToken();
//            Intent intent1 = new Intent(getSelfActivity(), MainActivity.class);
//            startActivity(intent1);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=receiveBroadCast) {
            unregisterReceiver(receiveBroadCast);
        }
        EventBus.getDefault().unregister(this);
    }
}
