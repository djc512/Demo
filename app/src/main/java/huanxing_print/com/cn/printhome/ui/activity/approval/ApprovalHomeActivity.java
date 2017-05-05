package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class ApprovalHomeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private LinearLayout ll_back;
    private ImageView iv_approval;
    private LinearLayout ll_approval;
    private LinearLayout ll_submit;
    private LinearLayout ll_send;
    private LinearLayout ll_purchase;
    private LinearLayout ll_reimburse;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_approval_home);
        initView();
        initListener();
    }

    private void initListener() {
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_approval = (ImageView) findViewById(R.id.iv_approval);
        ll_approval = (LinearLayout) findViewById(R.id.ll_approval);
        ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_purchase = (LinearLayout) findViewById(R.id.ll_purchase);
        ll_reimburse = (LinearLayout) findViewById(R.id.ll_reimburse);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_approval://待我审批
                startActivity(new Intent(getSelfActivity(), ApprovalActivity.class));
                break;
            case R.id.ll_submit://提交审批
                break;
            case R.id.ll_send://抄送
                break;
            case R.id.ll_purchase://采购
                startActivity(new Intent(getSelfActivity(), ApprovalBuyActivity.class));
                break;
            case R.id.ll_reimburse://报销
                break;
        }
    }
}
