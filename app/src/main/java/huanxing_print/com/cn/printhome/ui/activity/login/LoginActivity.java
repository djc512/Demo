package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.model.login.WeiXinBean;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.callback.login.WeiXinCallback;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.HttpCallBackListener;
import huanxing_print.com.cn.printhome.util.HttpUtil;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static android.content.ContentValues.TAG;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private TextView tv_login;
    private EditText login_phone, login_pass;
    private TextView forget_pass, login_register;
    private ImageView iv_head, passShowIv, passNormalIv;
    private String phone, headImg;
    private boolean isLoginOutDialogShow;// 是否显示登出的Dialog

    public static boolean isForeground = false;
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private long exitTime = 0;
    private String password;
    private String openid;
    private boolean weiXinFlag;
    //微信登录
    private IWXAPI api;
    private ReceiveBroadCast receiveBroadCast;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected boolean isNeedLocate() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_login);
        initViews();
        //initData();
    }
    @Override
    public void onResume() {
        super.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("authlogin");
        registerReceiver(receiveBroadCast, filter);
    }

    private void initData() {
        phone = baseApplication.getPhone();
        headImg = baseApplication.getHeadImg();
        if (!ObjectUtils.isNull(phone)) {
            login_phone.setText(phone);
        }

    }

    private void initViews() {
        login_phone = (EditText) findViewById(R.id.login_user);
        login_pass = (EditText) findViewById(R.id.login_pass);
        forget_pass = (TextView) findViewById(R.id.forget_pass);
        login_register = (TextView) findViewById(R.id.login_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        passShowIv = (ImageView) findViewById(R.id.pass_show);
        passNormalIv = (ImageView) findViewById(R.id.pass_normal);
        passShowIv.setOnClickListener(this);
        passNormalIv.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        forget_pass.setOnClickListener(this);
        login_register.setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String name = login_phone.getText().toString().trim();
                password = login_pass.getText().toString().trim();
                if (isUserNameAndPwdVali(name, password)) {
                    DialogUtils.showProgressDialog(getSelfActivity(), "正在登录中").show();
                    LoginRequset.login(getSelfActivity(), name, password, loginCallback);
                }
                //jumpActivity(MainActivity.class);
                break;
            case R.id.login_register://跳转注册界面
                jumpActivity(RegisterActivity.class);
                break;

            case R.id.forget_pass://跳转修改密码界面
                jumpActivity(ForgetPasswodActivity.class);
                break;
            case R.id.pass_normal:
                passShowIv.setVisibility(View.VISIBLE);
                passNormalIv.setVisibility(View.GONE);
                login_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case R.id.pass_show:
                passShowIv.setVisibility(View.GONE);
                passNormalIv.setVisibility(View.VISIBLE);
                login_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case R.id.ll_weixin:
                if (CommonUtils.isWeixinAvilible(getSelfActivity())) {
                    weChatAuth();
                } else {
                    ToastUtil.doToast(getSelfActivity(), "您还没有安装微信，请先安装微信客户端");
                }
                break;

            default:
                break;
        }
    }

    //登录接口返回
    private LoginCallback loginCallback = new LoginCallback() {

        @Override
        public void success(LoginBean loginBean) {

            baseApplication.setHasLoginEvent(true);
            DialogUtils.closeProgressDialog();
            if (!ObjectUtils.isNull(loginBean)) {
                String loginToken = loginBean.getLoginToken();
                baseApplication.setLoginToken(loginToken);
                LoginBeanItem userInfo = loginBean.getMemberInfo();
                if (!ObjectUtils.isNull(userInfo)) {
                    baseApplication.setPhone(userInfo.getMobileNumber());
                    baseApplication.setSex(userInfo.getSex());
                    baseApplication.setNickName(userInfo.getNickName());
                    baseApplication.setHeadImg(userInfo.getFaceUrl());
                    baseApplication.setPassWord(password);
                    jumpActivity(MainActivity.class);
                    finishCurrentActivity();
                }
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);

        }

        @Override
        public void connectFail() {
            Logger.d("connectFail:");
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

    };

    /*
     * 判断用户名和密码是否有效
     */
    private boolean isUserNameAndPwdVali(String name, String psd) {
        if (ObjectUtils.isNull(name)) {
            toast(getStringFromResource(R.string.phone_no_null));
            return false;
        } else if (!CommonUtils.isPhone(name)) {
            toast(getStringFromResource(R.string.phone_format_error));
            return false;
        } else if (ObjectUtils.isNull(psd)) {
            toast(getStringFromResource(R.string.psd_no_null));
            return false;
        } else if (psd.length() < 6) {
            toast(getStringFromResource(R.string.phone_set_null));
            return false;
        }
        return true;
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
                    Log.d(TAG, "-----获取到的json数据1-----" + jsonObject.toString());
                    String access_token = jsonObject.getString("access_token");
                    Log.d(TAG, "--------获取到的access_token的地址--------" + access_token);
                    openid = jsonObject.getString("openid");
                    String refresh_token = jsonObject.getString("refresh_token");
                    if (!access_token.equals("")) {
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
                    String sex = jsonObject.getString("sex");
                    String province = jsonObject.getString("province");
                    String city = jsonObject.getString("city");
                    String country = jsonObject.getString("country");
                    String headimgurl = jsonObject.getString("headimgurl");
                    String privilege = jsonObject.getString("privilege");
                    String unionId = jsonObject.getString("unionid");
                    LoginRequset.loginWeiXin(getSelfActivity(), city, country,
                            headimgurl, nickName, openid,
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
    private WeiXinCallback weiXinCallback = new WeiXinCallback() {

        @Override
        public void success(WeiXinBean weiXinBean) {

            baseApplication.setHasLoginEvent(true);
            DialogUtils.closeProgressDialog();
            if (!ObjectUtils.isNull(weiXinBean)) {
                String wechatId = weiXinBean.getWechatId();
                baseApplication.setWechatId(wechatId);
                weiXinFlag = weiXinBean.isLoginFlag();
                if (weiXinFlag) {
                    String loginToken = weiXinBean.getLoginResult().getLoginToken();
                    baseApplication.setLoginToken(loginToken);
                    LoginBeanItem userInfo = weiXinBean.getLoginResult().getMemberInfo();
                    if (!ObjectUtils.isNull(userInfo)) {
                        baseApplication.setPhone(userInfo.getMobileNumber());
                        baseApplication.setSex(userInfo.getSex());
                        baseApplication.setNickName(userInfo.getNickName());
                        baseApplication.setHeadImg(userInfo.getFaceUrl());
                        baseApplication.setPassWord(password);
                        //baseApplication.setWechatId(userInfo.getWechatId());
                        jumpActivity(MainActivity.class);
                        finishCurrentActivity();
                    }
                } else {
                    jumpActivity(RegisterActivity.class);
                }
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);

        }

        @Override
        public void connectFail() {
            Logger.d("connectFail:");
            DialogUtils.closeProgressDialog();
            toastConnectFail();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityHelper.getInstance().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiveBroadCast);
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
