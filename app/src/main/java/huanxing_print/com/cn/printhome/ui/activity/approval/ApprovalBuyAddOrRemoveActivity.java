package huanxing_print.com.cn.printhome.ui.activity.approval;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;


/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class ApprovalBuyAddOrRemoveActivity extends BaseActivity implements View.OnClickListener{

    public Context mContext;


    ImageView iv_user_name;
    ImageView iv_back;
    Button btn_agree;
    Button btn_bohui;
    Button btn_commit;
    TextView iv_name;
    TextView tv_use;
    TextView tv_number;
    TextView tv_section;
    TextView tv_detail;
    TextView iv_isapproval;
    TextView tv_money;
    TextView tv_overtime;
    TextView tv_use_name;

    LinearLayout ll_commit;
    LinearLayout bt_reject_agree;

    ListView ll_person;

    boolean isRequestMoney =false;


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_buy_add_or_move);
        CommonUtils.initSystemBar(this);
        mContext = ApprovalBuyAddOrRemoveActivity.this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        btn_commit.setOnClickListener(this);
        btn_bohui.setOnClickListener(this);
        btn_agree.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initData() {
        switch (getIntent().getStringExtra("what")){
            case "1":
                ll_commit.setVisibility(View.GONE);
                bt_reject_agree.setVisibility(View.VISIBLE);

                break;
            case "2":
                ll_commit.setVisibility(View.VISIBLE);
                bt_reject_agree.setVisibility(View.GONE);
                break;
            case "3":
                ll_commit.setVisibility(View.VISIBLE);
                bt_reject_agree.setVisibility(View.GONE);
                break;
            default:
                break;

        }

    }

    private void initView() {
        iv_isapproval = (TextView) findViewById(R.id.iv_isapproval);
        iv_name = (TextView) findViewById(R.id.iv_name);
        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_overtime = (TextView) findViewById(R.id.tv_overtime);
        tv_use_name = (TextView) findViewById(R.id.tv_use_name);

        ll_person = (ListView) findViewById(R.id.ll_person);
        iv_user_name = (ImageView) findViewById(R.id.iv_user_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_bohui = (Button) findViewById(R.id.btn_bohui);
        btn_agree = (Button) findViewById(R.id.btn_agree);

        ll_commit = (LinearLayout) findViewById(R.id.ll_remove);
        bt_reject_agree = (LinearLayout) findViewById(R.id.bt_reject_agree);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.bt_commit:
                break;
            case R.id.btn_bohui:
                finishCurrentActivity();
                //同时反馈数据给服务器


                break;
            case R.id.btn_agree:
                //startActivity(new Intent(getSelfActivity(), HandWriteActivity.class));
                break;
            default:
                break;
        }

    }



}
