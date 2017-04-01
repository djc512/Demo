package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.RechargeBean;
import huanxing_print.com.cn.printhome.net.callback.my.RechargeCallBack;
import huanxing_print.com.cn.printhome.net.request.my.RechargeRequset;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class BillDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_billdetail_type;
    private TextView tv_billdetail_num;
    private TextView tv_billdetail_time;
    private TextView tv_billdetail_money;
    private TextView tv_billdetail_dealtype;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activty_billitem_detail);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        //获取订单详情
        RechargeRequset.getRequest(getSelfActivity(), "", new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_billdetail_type = (TextView) findViewById(R.id.tv_billdetail_paytype);
        tv_billdetail_num = (TextView) findViewById(R.id.tv_billdetail_num);
        tv_billdetail_time = (TextView) findViewById(R.id.tv_billdetail_time);
        tv_billdetail_money = (TextView) findViewById(R.id.tv_billdetail_money);
        tv_billdetail_dealtype = (TextView) findViewById(R.id.tv_billdetail_dealtype);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }

    public class MyCallBack extends RechargeCallBack {

        @Override
        public void success(String msg, RechargeBean bean) {
            tv_billdetail_money.setText(bean.getAmount());
            tv_billdetail_dealtype.setText("交易类型:"+"");
            tv_billdetail_type.setText("交易方式:"+"");
            tv_billdetail_num.setText("交易号:"+"");
            tv_billdetail_time.setText("交易时间"+"");
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
