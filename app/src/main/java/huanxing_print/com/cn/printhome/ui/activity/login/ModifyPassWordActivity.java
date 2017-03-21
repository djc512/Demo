package huanxing_print.com.cn.printhome.ui.activity.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

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
    private ImageView iv_modify_back;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        initView();
        initData();
        setListener();
    }


    private void setListener() {
        btn_modify_ok.setOnClickListener(this);
        iv_modify_look.setOnClickListener(this);
        iv_modify_look1.setOnClickListener(this);
        iv_modify_back.setOnClickListener(this);
    }
    private void initData() {

    }

    private void initView() {
        et_modify_pwd = (EditText) findViewById(R.id.et_modify_pwd);
        iv_modify_look = (ImageView) findViewById(R.id.iv_modify_look);
        et_modify_pwd1 = (EditText) findViewById(R.id.et_modify_pwd1);
        iv_modify_look1 = (ImageView) findViewById(R.id.iv_modify_look1);
        iv_modify_back = (ImageView) findViewById(R.id.iv_modify_back);
        btn_modify_ok = (Button) findViewById(R.id.btn_modify_ok);
    }

    private boolean isLook = true;
    private boolean isLook1= true;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_modify_back:
                finish();
                break;
            case R.id.btn_modify_ok:
                pwd = et_modify_pwd.getText().toString().trim();
                pwd1 = et_modify_pwd1.getText().toString().trim();

                if (ObjectUtils.isNull(pwd)|| ObjectUtils.isNull(pwd1)) {
                    ToastUtil.showToast(this,"请先输入密码");
                    return;
                }
                if (pwd != pwd1){
                    ToastUtil.showToast(this,"两次密码不一致");
                    return;
                }
                break;
            case R.id.iv_modify_look:
                if(isLook){
                    et_modify_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_modify_look.setBackgroundResource(R.drawable.colse_2x);
                }else {
                    et_modify_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_modify_look.setBackgroundResource(R.drawable.look_2x);
                }
                isLook =!isLook;
                break;
             case R.id.iv_modify_look1:
                 if(isLook1){
                     et_modify_pwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                     iv_modify_look1.setBackgroundResource(R.drawable.colse_2x);
                 }else {
                     et_modify_pwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                     iv_modify_look1.setBackgroundResource(R.drawable.look_2x);
                 }
                 isLook1 =!isLook1;
                 break;
        }
    }
}
