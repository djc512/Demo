package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.OrderDetailBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.my.OrderDetailRequest;
import huanxing_print.com.cn.printhome.ui.adapter.OrderItemDetailAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private RecyclerView rv_order_item;
    private TextView tv_order_id;
    private TextView tv_order_channel;
    private TextView tv_order_time;
    private TextView tv_order_address;
    private TextView tv_order_totalprice;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        OrderDetailRequest.getOrderDetail(getSelfActivity(), id, new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_order_item = (RecyclerView) findViewById(R.id.rv_order_item);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_order_channel = (TextView) findViewById(R.id.tv_order_channel);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_order_address = (TextView) findViewById(R.id.tv_order_address);
        tv_order_totalprice = (TextView) findViewById(R.id.tv_order_totalprice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }

    public class MyCallBack extends OrderDetailCallBack {

        @Override
        public void success(String msg, OrderDetailBean bean) {
            OrderDetailBean.OrderInfoBean orderInfo = bean.getOrderInfo();
            List<OrderDetailBean.PrintFilesBean> printFiles = bean.getPrintFiles();

            int id = orderInfo.getId();//订单号
            String address = orderInfo.getAddress();//打印地址
            String channel = orderInfo.getChannel();//支付方式
            double totalAmount = orderInfo.getTotalAmount();//总价

            tv_order_address.setText("打印地址:"+address);
            tv_order_channel.setText("支付方式:"+channel);
            tv_order_id.setText("订单号:"+id);
            tv_order_totalprice.setText("￥"+totalAmount);

            OrderItemDetailAdapter adapter = new OrderItemDetailAdapter(getSelfActivity(), printFiles);
            rv_order_item.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
            rv_order_item.setAdapter(adapter);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
