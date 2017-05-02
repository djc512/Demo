package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.ui.activity.login.ForgetPasswodActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.util.AppUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private ImageView iv_change_pwd;
    private ImageView iv_change_print;
    private ImageView iv_xieyi;
    private TextView tv_set_version;
    private ImageView iv_version;
    private LinearLayout ll_set_pwd;
    private LinearLayout ll_set_print;
    private LinearLayout ll_set_xy;
    private RelativeLayout rl_set_version;

    private String version;
    private String ApkUrl;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_user_setting);
        initView();
        setListener();
    }

    private void initView() {
        version = AppUtils.getVersionName(getSelfActivity());
        ApkUrl = baseApplication.getApkUrl();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_change_pwd = (ImageView) findViewById(R.id.iv_change_pwd);
        iv_change_print = (ImageView) findViewById(R.id.iv_change_print);
        iv_xieyi = (ImageView) findViewById(R.id.iv_xieyi);
        tv_set_version = (TextView) findViewById(R.id.tv_set_version);
        iv_version = (ImageView) findViewById(R.id.iv_version);
        ll_set_pwd = (LinearLayout) findViewById(R.id.ll_set_pwd);
        ll_set_print = (LinearLayout) findViewById(R.id.ll_set_print);
        ll_set_xy = (LinearLayout) findViewById(R.id.ll_set_xy);
        rl_set_version = (RelativeLayout) findViewById(R.id.rl_set_version);

        tv_set_version.setText("( V"+version+" )");
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_change_pwd.setOnClickListener(this);
        iv_change_print.setOnClickListener(this);
        iv_xieyi.setOnClickListener(this);
        findViewById(R.id.ll_loginout).setOnClickListener(this);
        findViewById(R.id.rl_set_version).setOnClickListener(this);
        ll_set_pwd.setOnClickListener(this);
        ll_set_print.setOnClickListener(this);
        ll_set_xy.setOnClickListener(this);
        rl_set_version.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.ll_set_pwd://跳转到修改密码
                jumpActivity(ForgetPasswodActivity.class);
                break;
            case R.id.ll_set_print://跳转设置打印机
                jumpActivity(SetDefaultPrinterActivity.class);
                break;
            case R.id.ll_set_xy://打印协议
                startActivity(new Intent(getSelfActivity(), XieYiActivity.class));
                break;
            case R.id.rl_set_version://版本更新
                //startActivity(new Intent(getSelfActivity(), XieYiActivity.class));
               if (baseApplication.isNewApp()){
                   toast("当前版本为最新版本");
               }else{
                   DialogUtils.showTipsDialog(getSelfActivity(), getResources().getString(R.string.dlg_content_loginout),
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
            case R.id.ll_loginout:// 退出当前账号
                DialogUtils.showTipsDialog(getSelfActivity(), getResources().getString(R.string.dlg_content_loginout),
                        new DialogUtils.TipsDialogCallBack() {
                            @Override
                            public void ok() {
                                DialogUtils.showProgressDialog(getSelfActivity(), "正在退出登录").show();
                                LoginRequset.loginOut(getSelfActivity(), baseApplication.getLoginToken(), loginoutcallback);

                            }
                        }).show();
                break;
            default:
                break;
        }
    }

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
            ActivityHelper.getInstance().finishAllActivity();
            activityExitAnim();
            jumpActivityNoAnim(LoginActivity.class, false);
        }
    };
}
