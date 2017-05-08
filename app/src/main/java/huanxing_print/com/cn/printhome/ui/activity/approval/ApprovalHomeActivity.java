package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class ApprovalHomeActivity extends BaseActivity implements View.OnClickListener {
    private View ll_back;
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
        ll_approval.setOnClickListener(this);
        ll_submit.setOnClickListener(this);
        ll_send.setOnClickListener(this);
        ll_purchase.setOnClickListener(this);
        ll_reimburse.setOnClickListener(this);
    }

    private void initView() {
        ll_back = findViewById(R.id.ll_back);
        ll_approval = (LinearLayout) findViewById(R.id.ll_approval);
        ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_purchase = (LinearLayout) findViewById(R.id.ll_purchase);
        ll_reimburse = (LinearLayout) findViewById(R.id.ll_reimburse);

        //结束
        ll_back.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back://返回
                finishCurrentActivity();
                break;
            case R.id.ll_approval://待我审批
                startActivity(new Intent(this, ApprovalActivity.class));
                break;
            case R.id.ll_submit://提交审批
                startActivity(new Intent(this, MySponsorListActivity.class));
                break;
            case R.id.ll_send://抄送
                startActivity(new Intent(this, CopyToMeActivity.class));
                break;
            case R.id.ll_purchase://采购
                startActivity(new Intent(this, AddPurchaseApprovalActivity.class));
                break;
            case R.id.ll_reimburse://报销
                startActivity(new Intent(this, AddExpenseApprovalActivity.class));
                break;
            default:
                break;
        }
    }
}
