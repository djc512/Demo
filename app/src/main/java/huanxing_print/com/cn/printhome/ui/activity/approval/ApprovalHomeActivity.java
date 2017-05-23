package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class ApprovalHomeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_approval;
    private LinearLayout ll_submit;
    private LinearLayout ll_send;
    private LinearLayout ll_purchase;
    private LinearLayout ll_reimburse;
    private TextView tv_approver_num,tv_copyer_num,tv_initiator_num;
    private int approverNum;//待我审批
    private int copyerNum;//抄送我的
    private int initiatorNum;//我发起的

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_approval_home);
        EventBus.getDefault().register(this);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        ll_approval.setOnClickListener(this);
        ll_submit.setOnClickListener(this);
        ll_send.setOnClickListener(this);
        ll_purchase.setOnClickListener(this);
        ll_reimburse.setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    private void initView() {
        ll_approval = (LinearLayout) findViewById(R.id.ll_approval);
        ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_purchase = (LinearLayout) findViewById(R.id.ll_purchase);
        ll_reimburse = (LinearLayout) findViewById(R.id.ll_reimburse);
        tv_approver_num= (TextView) findViewById(R.id.tv_approver_num);
        tv_copyer_num= (TextView) findViewById(R.id.tv_copyer_num);
        tv_initiator_num= (TextView) findViewById(R.id.tv_initiator_num);

    }


    private void initData() {
        approverNum = getIntent().getIntExtra("approverNum",0);
        copyerNum = getIntent().getIntExtra("copyerNum",0);
        initiatorNum = getIntent().getIntExtra("initiatorNum",0);
        if (approverNum>0){
            tv_approver_num.setVisibility(View.VISIBLE);
            tv_approver_num.setText(approverNum+"");
        }else{
            tv_approver_num.setVisibility(View.GONE);
        }
//        if (copyerNum>0){
//            tv_copyer_num.setVisibility(View.VISIBLE);
//            tv_copyer_num.setText(copyerNum+"");
//        }else{
//            tv_copyer_num.setVisibility(View.GONE);
//        }
//        if (initiatorNum>0){
//            tv_initiator_num.setVisibility(View.VISIBLE);
//            tv_initiator_num.setText(initiatorNum+"");
//        }else{
//            tv_initiator_num.setVisibility(View.GONE);
//        }
    }
    @Subscriber(tag = "refreshApprovalNum")
    private void setRefreshApprovalNum() {
        approverNum --;
        if (approverNum>0){
            tv_approver_num.setVisibility(View.VISIBLE);
            tv_approver_num.setText(approverNum+"");
        }else{
            tv_approver_num.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back://返回
                finishCurrentActivity();
                break;
            case R.id.ll_approval://待我审批
                Intent intent = new Intent(this, ApprovalActivity.class);
                intent.putExtra("approverNum",approverNum);
                startActivity(intent);
                break;
            case R.id.ll_submit://我发起的
                startActivity(new Intent(this, MySponsorListActivity.class));
                break;
            case R.id.ll_send://抄送
                startActivity(new Intent(this, CopyToMeActivity.class));
                break;
            case R.id.ll_purchase://采购
//                startActivity(new Intent(this, AddPurchaseApprovalActivity.class));
                startActivity(new Intent(this, AddPurchaseApprovalActivity2.class));
                break;
            case R.id.ll_reimburse://报销
//                startActivity(new Intent(this, AddExpenseApprovalActivity.class));
                startActivity(new Intent(this, AddExpenseApprovalActivity2.class));
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
