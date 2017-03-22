package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.login.VeryCodeBean;
import huanxing_print.com.cn.printhome.net.callback.login.VeryCodeCallback;
import huanxing_print.com.cn.printhome.net.request.login.VeryCodeRequest;
import huanxing_print.com.cn.printhome.util.RegexUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class ForgetPasswodActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_forget_phone;
    private EditText et_forget_VeryCode;
    private TextView tv_forget_VeryCode;
    private Button btn_forget_next;
    private String phone;
    private CountDownTimer time;
    private LinearLayout ll_back;
    private String veryCode;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_forget_phone = (EditText) findViewById(R.id.et_forget_phone);
        et_forget_VeryCode = (EditText) findViewById(R.id.et_forget_VeryCode);
        tv_forget_VeryCode = (TextView) findViewById(R.id.tv_forget_VeryCode);
        btn_forget_next = (Button) findViewById(R.id.btn_forget_next);

    }
    private void setListener() {
        btn_forget_next.setOnClickListener(this);
        tv_forget_VeryCode.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_next:
                veryCode = et_forget_VeryCode.getText().toString().trim();
//                if(ObjectUtils.isNull(veryCode)){
//                    ToastUtil.showToast(ForgetPasswodActivity.this,"请输入验证码");
//                    return;
//                }
                Intent intent = new Intent(getSelfActivity(),ModifyPassWordActivity.class);
                intent.putExtra("veryCode",veryCode);
                intent.putExtra("phoneNum",phone);
                startActivity(intent);
                break;

            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_forget_VeryCode://获取验证码

                phone = et_forget_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegexUtils.isMobileExact(phone)){
                    ToastUtil.doToast(getSelfActivity(),"手机号码格式不正确");
                    return;
                }
                setTimeCount();
                getVeryCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVeryCode() {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在获取验证码").show();

        VeryCodeRequest.getVeryCode(this, "2",phone, new VeryCodeCallback() {

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }

            @Override
            public void success(String msg, VeryCodeBean bean) {
                DialogUtils.closeProgressDialog();
                toast("获取验证码成功");
            }
        });
    }
    /**
     * 设置倒计时
     */
    private void setTimeCount() {
        time = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_forget_VeryCode.setText(""+ millisUntilFinished / 1000);
             }
            @Override
            public void onFinish() {
                tv_forget_VeryCode.setText("重新获取验证码");
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_forget_VeryCode.setText("获取验证码");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (time != null) {
            time.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
        }
    }
}
