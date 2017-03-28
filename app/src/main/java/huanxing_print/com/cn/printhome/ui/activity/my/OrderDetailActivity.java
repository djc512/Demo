package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.OrderDetailBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.my.OrderDetailRequest;
import huanxing_print.com.cn.printhome.ui.adapter.OrderItemDetailAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private RecyclerView rv_order_item;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        OrderDetailRequest.getOrderDetail(getSelfActivity(),"11111111111111",new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_order_item = (RecyclerView) findViewById(R.id.rv_order_item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.ll_back:
                finish();
                break;
        }
    }
    public class MyCallBack extends OrderDetailCallBack{

        @Override
        public void success(String msg, OrderDetailBean bean) {
            OrderDetailBean.OrderInfoBean orderInfo = bean.getOrderInfo();
            List<OrderDetailBean.PrintFilesBean> printFiles = bean.getPrintFiles();

            OrderItemDetailAdapter adapter = new OrderItemDetailAdapter(getSelfActivity(),printFiles);
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
