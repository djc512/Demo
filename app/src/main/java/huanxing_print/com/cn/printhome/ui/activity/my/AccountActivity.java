package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderIdCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongzhiRequest;
import huanxing_print.com.cn.printhome.net.request.my.OrderIdRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountCZAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_money;
    private Button btn_chongzhi;
    private LinearLayout ll_back;
    private TextView tv_account_record;
    private RecyclerView rv_account;
    private AccountCZAdapter adapter;
    private List<ChongZhiBean> listBean = new ArrayList<>();

    private String rechargeAmout;
    private ChongZhiBean chongZhiBean;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_account);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        rv_account = (RecyclerView) findViewById(R.id.rv_account);

    }

    private void initData() {
        tv_money.setText(getIntent().getStringExtra("totleBalance"));
        //通过接口获取充值数据
        ChongzhiRequest.getChongZhi(getSelfActivity(), new MyChongzhiCallBack());
    }

    private void setListener() {
        tv_account_record.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_chongzhi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chongzhi:
                if (ObjectUtils.isNull(rechargeAmout)) {//如果没有选择充值数
                    ToastUtil.doToast(getSelfActivity(), "请先选择充值金额");
                    return;
                }
                getOrderId();
                break;
            case R.id.tv_account_record://充值记录
                startActivity(new Intent(getSelfActivity(), AccountRecordActivity.class));
                break;
            case R.id.ll_back://返回
                finish();
                break;
        }
    }

    /**
     * 获取订单号
     */
    private void getOrderId() {
        OrderIdRequest.getOrderId(getSelfActivity(), rechargeAmout, new OrderIdCallBack() {
            @Override
            public void success(String msg, String data) {
                toast("请求成功");
                Intent intent = new Intent(getSelfActivity(), PayActivity.class);
                intent.putExtra("orderId", data);
                intent.putExtra("rechargeAmout", rechargeAmout);
                startActivity(intent);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });

    }

    public class MyChongzhiCallBack extends ChongzhiCallBack {

        @Override
        public void success(String msg, List<ChongZhiBean> list) {
            listBean = list;
            toast("获取成功");
            rv_account.setLayoutManager(new GridLayoutManager(getSelfActivity(), 2));
            adapter = new AccountCZAdapter(getSelfActivity(), listBean);
            rv_account.setAdapter(adapter);

            //条目点击事件
            adapter.setOnItemClickLitener(new AccountCZAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    adapter.setSeclection(position);
                    adapter.notifyDataSetChanged();
                    chongZhiBean = listBean.get(position);
                    //充值金额
                    rechargeAmout = chongZhiBean.getRechargeAmout();
                }
            });
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
