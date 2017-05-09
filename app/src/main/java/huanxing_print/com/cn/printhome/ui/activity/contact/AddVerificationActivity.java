package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/3.
 * desc  : 添加好友 验证页面
 */

public class AddVerificationActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_hint_content;
    private FriendSearchInfo friendSearchInfo;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_verification);
        friendSearchInfo = getIntent().getParcelableExtra("verification");
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
                request();
                break;
        }
    }

    private void request() {
        if(null != friendSearchInfo && !et_hint_content.getText().toString().isEmpty()) {
            DialogUtils.showProgressDialog(this, "添加好友验证中").show();
            String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("memberId", friendSearchInfo.getMemberId());
            params.put("note", et_hint_content.getText().toString());
            FriendManagerRequest.searchAddFriend(this, token, params, nullCallback);
        }
    }

    NullCallback nullCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(AddVerificationActivity.this, "添加成功");
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(AddVerificationActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };
}
