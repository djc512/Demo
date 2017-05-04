package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by wanghao on 2017/5/3.
 * desc  : 添加好友 验证页面
 */

public class AddVerificationActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_hint_content;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_verification);
        initView();
        setListener();
    }

    private void initView() {
        et_hint_content = (EditText) findViewById(R.id.et_hint_content);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.verification_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.verification_send:
                ToastUtil.doToast(this,"添加好友验证请求发送中...");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
