package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.ForgetPasswodActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

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
    private TextView tv_set_loginout;
    private LinearLayout ll_set_pwd;
    private LinearLayout ll_set_print;
    private LinearLayout ll_set_xy;
    private RelativeLayout rl_set_version;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_setting);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_change_pwd = (ImageView) findViewById(R.id.iv_change_pwd);
        iv_change_print = (ImageView) findViewById(R.id.iv_change_print);
        iv_xieyi = (ImageView) findViewById(R.id.iv_xieyi);
        tv_set_version = (TextView) findViewById(R.id.tv_set_version);
        iv_version = (ImageView) findViewById(R.id.iv_version);
        tv_set_loginout = (TextView) findViewById(R.id.tv_set_loginout);
        ll_set_pwd = (LinearLayout) findViewById(R.id.ll_set_pwd);
        ll_set_print = (LinearLayout) findViewById(R.id.ll_set_print);
        ll_set_xy = (LinearLayout) findViewById(R.id.ll_set_xy);
        rl_set_version = (RelativeLayout) findViewById(R.id.rl_set_version);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_change_pwd.setOnClickListener(this);
        iv_change_print.setOnClickListener(this);
        iv_xieyi.setOnClickListener(this);
        tv_set_loginout.setOnClickListener(this);

        ll_set_pwd.setOnClickListener(this);
        ll_set_print.setOnClickListener(this);
        ll_set_xy.setOnClickListener(this);
        rl_set_version.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_set_pwd://跳转到修改密码
                startActivity(new Intent(getSelfActivity(), ForgetPasswodActivity.class));
                break;
            case R.id.ll_set_print://跳转设置打印机

                break;
            case R.id.tv_set_loginout://退出登录

                break;
            case R.id.ll_set_xy://打印协议
                startActivity(new Intent(getSelfActivity(), XieYiActivity.class));
                break;
        }
    }
}
