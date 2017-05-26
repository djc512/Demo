package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.request.my.UpdatePersonInfoRequest;
import huanxing_print.com.cn.printhome.net.request.register.RegisterRequst;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.time.ScheduledHandler;
import huanxing_print.com.cn.printhome.util.time.ScheduledTimer;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyModifyPhoneActivty extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView iv_modifyName_finish,getCodeTv,tv_title;
    private EditText et_phone,et_code;
    private ImageView iv_phone_delete,iv_code_detele;
    private String phone,code;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_useinfo_midifyphone);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        if (!ObjectUtils.isNull(phone)) {
            et_phone.setText(phone);
            tv_title.setText("更新手机号");
        }else{
            tv_title.setText("绑定手机号");
        }

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_phone_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_phone_delete.setVisibility(View.GONE);
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
                if (start > 0) {
                    iv_code_detele.setVisibility(View.VISIBLE);
                } else {
                    iv_code_detele.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_modifyName_finish = (TextView) findViewById(R.id.iv_modifyName_finish);
        getCodeTv = (TextView) findViewById(R.id.code_btn);
        tv_title= (TextView) findViewById(R.id.tv_title);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code= (EditText) findViewById(R.id.et_code);
        iv_phone_delete = (ImageView) findViewById(R.id.iv_phone_delete);
        iv_code_detele = (ImageView) findViewById(R.id.iv_code_detele);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_modifyName_finish.setOnClickListener(this);
        iv_phone_delete.setOnClickListener(this);
        iv_code_detele.setOnClickListener(this);
        getCodeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.iv_modifyName_finish:
                phone = et_phone.getText().toString().trim();
                code = et_code.getText().toString().trim();
                if(ObjectUtils.isNull(phone)){
                    ToastUtil.doToast(getSelfActivity(),"请输入手机号");
                    return;
                }
                if(ObjectUtils.isNull(code)){
                    ToastUtil.doToast(getSelfActivity(),"请输入验证码");
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("mobileNumber", phone);
                params.put("validCode", code);
                DialogUtils.showProgressDialog(getSelfActivity(), "正在保存").show();
                UpdatePersonInfoRequest.update(getSelfActivity(),  baseApplication.getLoginToken(),params, callback);

                break;
            case R.id.code_btn://
                getVerCode();
                break;
            case R.id.iv_phone_delete://
                et_phone.setText("");
                break;
            case R.id.iv_code_detele://
                et_code.setText("");
                break;
            default:
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getVerCode() {
        phone = et_phone.getText().toString().trim();
        if (ObjectUtils.isNull(phone)) {
            toast("手机号不能为空");
            return;
        }
        if (!CommonUtils.isPhone(phone) || phone.length() < 11) {
            toast("手机号码格式有误");
            return;
        }
        et_phone.setEnabled(false);
        if (!ObjectUtils.isNull(phone)) {
            getCodeTv.setClickable(false);
            DialogUtils.showProgressDialog(getSelfActivity(), "正在获取验证码").show();
            RegisterRequst.getVerCode(getSelfActivity(), baseApplication.getLoginToken(), phone, 4, getVerCodeCallback);
        }
    }


    private GetVerCodeCallback getVerCodeCallback = new GetVerCodeCallback() {

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
            getCodeTv.setClickable(true);
            et_phone.setEnabled(true);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            getCodeTv.setClickable(true);
            et_phone.setEnabled(true);
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            toast("获取验证码成功");
            codeCountdown();
        }
    };
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
            baseApplication.setPhone(phone);
            Intent carlist = new Intent(getSelfActivity() , MyActivity.class);
            carlist.putExtra("phone", phone);
            setResult(104, carlist);
            finishCurrentActivity();
            EventBus.getDefault().post(phone, "phone");
            //EventBus.getDefault().post(new UpdateEvent());
        }
    };

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
                et_phone.setEnabled(true);
            }
        }, 0, 1000, 60);
        scheduledTimer.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
