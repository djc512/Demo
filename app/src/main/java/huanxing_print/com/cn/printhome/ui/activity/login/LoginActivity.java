package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.listener.EmsCallBackListener;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.model.login.WeiXinBean;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.callback.login.WeiXinCallback;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.net.request.register.RegisterRequst;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.HttpCallBackListener;
import huanxing_print.com.cn.printhome.util.HttpUtil;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.time.ScheduledHandler;
import huanxing_print.com.cn.printhome.util.time.ScheduledTimer;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static android.content.ContentValues.TAG;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private static final int REQUEST_SDCARD = 1;

    private TextView tv_login,getCodeTv;
    private EditText login_phone,et_code;
    private ImageView iv_phone_detele,iv_code_detele;
    private TextView  tv_register;
    private String phone;
    String name;
    String validCode;

    private long exitTime = 0;
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
        initData();
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
//        phone = baseApplication.getPhone();
//        if (!ObjectUtils.isNull(phone)) {
//            login_phone.setText(phone);
//        }
        login_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start>0){
                    iv_phone_detele.setVisibility(View.VISIBLE);
                }else{
                    iv_phone_detele.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start>0){
                    iv_code_detele.setVisibility(View.VISIBLE);
                }else{
                    iv_code_detele.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews() {
        login_phone = (EditText) findViewById(R.id.login_user);
        et_code= (EditText) findViewById(R.id.et_code);
        tv_register = (TextView) findViewById(R.id.tv_register);
        getCodeTv = (TextView) findViewById(R.id.code_btn);
        iv_phone_detele = (ImageView) findViewById(R.id.iv_phone_detele);
        iv_code_detele = (ImageView) findViewById(R.id.iv_code_detele);
        tv_login = (TextView) findViewById(R.id.tv_login);
        getCodeTv.setOnClickListener(this);
        iv_phone_detele.setOnClickListener(this);
        iv_code_detele.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                name = login_phone.getText().toString().trim();
                validCode = et_code.getText().toString().trim();
                if (isUserNameAndPwdVali(name,validCode)) {

                    DialogUtils.showProgressDialog(getSelfActivity(), "正在登录中").show();

                    LoginRequset.login(getSelfActivity(), name, validCode, loginCallback);
                }
//                jumpActivity(MainActivity.class);
                break;
            case R.id.tv_register://跳转注册界面
                jumpActivity(RegisterActivity.class);
                break;
            case R.id.code_btn:// 获取验证码
                getVerCode();
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
        public void success(final LoginBean loginBean) {
            //toast(""+loginBean.getMemberInfo().getEasemobId());
            //判断环信是否登录成功
            EMClient.getInstance().login(loginBean.getMemberInfo().getEasemobId(),loginBean.getMemberInfo().getEasemobId() , new EmsCallBackListener() {
                @Override
                public void onMainSuccess() {
                    baseApplication.setHasLoginEvent(true);
                    DialogUtils.closeProgressDialog();
                    if (!ObjectUtils.isNull(loginBean)) {
                        String loginToken = loginBean.getLoginToken();
                        baseApplication.setLoginToken(loginToken);
                        LoginBeanItem userInfo = loginBean.getMemberInfo();
                        if (!ObjectUtils.isNull(userInfo)) {
                            baseApplication.setPhone(userInfo.getMobileNumber());
                            baseApplication.setNickName(userInfo.getNickName());
                            baseApplication.setHeadImg(userInfo.getFaceUrl());
                            baseApplication.setEasemobId(userInfo.getEasemobId());
                            baseApplication.setUniqueId(userInfo.getUniqueId());
                            if (!ObjectUtils.isNull(userInfo.getWechatId())) {
                                baseApplication.setWechatId(userInfo.getWechatId());
                            }
                            jumpActivity(MainActivity.class);
                            finishCurrentActivity();
                        }
                    }

                }

                @Override
                public void onMainError(int i, String s) {
                    DialogUtils.closeProgressDialog();
                    toast("环信登录失败");

                }
            });

            /*baseApplication.setHasLoginEvent(true);
            DialogUtils.closeProgressDialog();
            if (!ObjectUtils.isNull(loginBean)) {
                String loginToken = loginBean.getLoginToken();
                baseApplication.setLoginToken(loginToken);
                LoginBeanItem userInfo = loginBean.getMemberInfo();
                if (!ObjectUtils.isNull(userInfo)) {
                    baseApplication.setPhone(userInfo.getMobileNumber());
                    baseApplication.setNickName(userInfo.getNickName());
                    baseApplication.setHeadImg(userInfo.getFaceUrl());
                    baseApplication.setEasemobId(userInfo.getEasemobId());
                    baseApplication.setUniqueId(userInfo.getUniqueId());
                    if (!ObjectUtils.isNull(userInfo.getWechatId())) {
                        baseApplication.setWechatId(userInfo.getWechatId());
                    }
                    jumpActivity(MainActivity.class);
                    finishCurrentActivity();
                }
            }*/
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
    private boolean isUserNameAndPwdVali(String name,String code) {
        if (ObjectUtils.isNull(name)) {
            toast(getStringFromResource(R.string.phone_no_null));
            return false;
        } else if (!CommonUtils.isPhone(name)) {
            toast(getStringFromResource(R.string.phone_format_error));
            return false;
        }else if (ObjectUtils.isNull(code)) {
            toast(getStringFromResource(R.string.code_no_null));
            return false;
        }
        return true;
    }


    /**
     * 获取验证码
     */
    private void getVerCode() {
        phone = login_phone.getText().toString();
        if (ObjectUtils.isNull(phone)) {
            toast("手机号不能为空");
            return;
        }
        if (!CommonUtils.isPhone(phone) || phone.length() < 11) {
            toast("手机号码格式有误");
            return;
        }
        login_phone.setEnabled(false);
        if (!ObjectUtils.isNull(phone)) {
            getCodeTv.setClickable(false);
            DialogUtils.showProgressDialog(getSelfActivity(), "正在获取验证码").show();
            RegisterRequst.getVerCode(getSelfActivity(), "0", phone, 0, getVerCodeCallback);
        }
    }


    private GetVerCodeCallback getVerCodeCallback = new GetVerCodeCallback() {

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
            getCodeTv.setClickable(true);
            login_phone.setEnabled(true);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            getCodeTv.setClickable(true);
            login_phone.setEnabled(true);
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            toast("获取验证码成功");
            codeCountdown();
        }
    };

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
                    baseApplication.setWeixinName(nickName);
                    String sex = jsonObject.getString("sex");
                    String province = jsonObject.getString("province");
                    String city = jsonObject.getString("city");
                    String country = jsonObject.getString("country");
                    String headImgurl = jsonObject.getString("headimgurl");
                    String privilege = jsonObject.getString("privilege");
                    String unionId = jsonObject.getString("unionid");
                    LoginRequset.loginWeiXin(getSelfActivity(), city, country,
                            headImgurl, nickName, openid,
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
                        baseApplication.setNickName(userInfo.getNickName());
                        baseApplication.setHeadImg(userInfo.getFaceUrl());
                        baseApplication.setEasemobId(userInfo.getEasemobId());
                        baseApplication.setUniqueId(userInfo.getUniqueId());
                        if (!ObjectUtils.isNull(userInfo.getWechatId())) {
                            baseApplication.setWechatId(userInfo.getWechatId());
                        }
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


    /**
     * 获取验证码成功倒计时
     */
    private void codeCountdown() {
        ScheduledTimer scheduledTimer = new ScheduledTimer(new ScheduledHandler() {
            @Override
            public void post(int times) {
                getCodeTv.setText((60 - times) + "秒");
            }

            @Override
            public void end() {
                getCodeTv.setText("重新获取");
                getCodeTv.setClickable(true);
                login_phone.setEnabled(true);
            }
        }, 0, 1000, 60);
        scheduledTimer.start();
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
