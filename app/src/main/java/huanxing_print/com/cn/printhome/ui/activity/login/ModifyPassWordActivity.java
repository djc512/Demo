package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.login.ModifyPasswordBean;
import huanxing_print.com.cn.printhome.net.callback.login.ModifyPasswordCallback;
import huanxing_print.com.cn.printhome.net.request.login.ModifyPassWordRequset;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class ModifyPassWordActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_modify_pwd;
    private ImageView iv_modify_look;
    private EditText et_modify_pwd1;
    private ImageView iv_modify_look1;
    private Button btn_modify_ok;
    private String pwd;
    private String pwd1;

    private String veryCode;//验证码
    private String phoneNum;//手机号
    private LinearLayout ll_back;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        Intent intent = getIntent();
        veryCode =intent.getStringExtra("veryCode");
        phoneNum =intent.getStringExtra("phoneNum");

        initView();
        initData();
        setListener();
    }
    private void initData() {
    }
    private void setListener() {
        btn_modify_ok.setOnClickListener(this);
        iv_modify_look.setOnClickListener(this);
        iv_modify_look1.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_modify_pwd = (EditText) findViewById(R.id.et_modify_pwd);
        iv_modify_look = (ImageView) findViewById(R.id.iv_modify_look);
        et_modify_pwd1 = (EditText) findViewById(R.id.et_modify_pwd1);
        iv_modify_look1 = (ImageView) findViewById(R.id.iv_modify_look1);
        btn_modify_ok = (Button) findViewById(R.id.btn_modify_ok);

    }
    private boolean isLook = true;
    private boolean isLook1 = true;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.btn_modify_ok:
                pwd = et_modify_pwd.getText().toString().trim();
                pwd1 = et_modify_pwd1.getText().toString().trim();

                if (ObjectUtils.isNull(pwd) || ObjectUtils.isNull(pwd1)) {
                    ToastUtil.doToast(ModifyPassWordActivity.this,"请先输入密码");
                    return;
                }
                if (!pwd1.equals(pwd)){
                    ToastUtil.doToast(ModifyPassWordActivity.this,"两次密码不一致");
                    return;
                }
                modifyPwd();
                finishCurrentActivity();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.iv_modify_look:
                if(isLook){
                    et_modify_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_modify_look.setBackgroundResource(R.drawable.btn_password_show);
                }else {
                    et_modify_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_modify_look.setBackgroundResource(R.drawable.btn_password_normal);
                }
                isLook =!isLook;

                break;
            case R.id.iv_modify_look1:
                if(isLook1){
                    et_modify_pwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_modify_look1.setBackgroundResource(R.drawable.btn_password_show);
                }else {
                    et_modify_pwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_modify_look1.setBackgroundResource(R.drawable.btn_password_normal);
                }
                isLook1 =!isLook1;
                break;
        }
    }

    /**
     * 修改密码
     */
    private void modifyPwd() {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在提交").show();
        ModifyPassWordRequset.modifyPwd(getSelfActivity(), veryCode, pwd1, phoneNum, new ModifyPasswordCallback() {
            @Override
            public void success(String msg, ModifyPasswordBean bean) {
                DialogUtils.closeProgressDialog();
                toast("修改成功");
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }
}
