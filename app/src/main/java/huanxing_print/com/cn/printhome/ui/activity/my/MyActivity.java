package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.image.HeadImageUploadRequest;
import huanxing_print.com.cn.printhome.net.request.my.UpdatePersonInfoRequest;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;


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
    private String oriPath = ConFig.IMG_CACHE_PATH + File.separator + "headImg.jpg";
    private ImageView iv_user_head;
    private LinearLayout ll_back;

    private File tempFile;
    private PopupWindow popupWindow;
    private LinearLayout ll_userInfo_name;
    private LinearLayout ll_userInfo_wechat;
    private String wechatId;
    private TextView tv_userInfo_wechat,tv_userInfo_nickname;
    private String cropImagePath;
    private String nickName;
    private Bitmap bitMap;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_my);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
    }

    /**
     * 获取用户信息
     */
    private void initData() {
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickName");
        wechatId = intent.getStringExtra("wechatId");
        BitmapUtils.displayImage(getSelfActivity(), baseApplication.getHeadImg(),
                R.drawable.iv_head, iv_user_head);
        if (!ObjectUtils.isNull(nickName)) {
            tv_userInfo_nickname.setText(nickName);
        }
        if (ObjectUtils.isNull(wechatId)) {
            tv_userInfo_wechat.setText("未绑定");
        } else {
            tv_userInfo_wechat.setText("已绑定");
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

        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_userInfo_name = (LinearLayout) findViewById(R.id.ll_userInfo_name);
        ll_userInfo_wechat = (LinearLayout) findViewById(R.id.ll_userInfo_wechat);
        tv_userInfo_nickname = (TextView) findViewById(R.id.tv_userInfo_nickname);
        tv_userInfo_wechat = (TextView) findViewById(R.id.tv_userInfo_wechat);
    }

    private void setListener() {
        iv_user_head.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        ll_userInfo_name.setOnClickListener(this);
        ll_userInfo_wechat.setOnClickListener(this);
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

            case R.id.ll_userInfo_name:
                Intent nameIntent = new Intent(getSelfActivity(), MyModifyNameActivty.class);
                nameIntent.putExtra("nickName", nickName);
                startActivityForResult(nameIntent, NAME_CODE);
                break;
            case R.id.ll_userInfo_wechat:

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
                    String name = intent.getStringExtra("nickName");
                    if (!ObjectUtils.isNull(name)) {
                        tv_userInfo_wechat.setText(name);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
