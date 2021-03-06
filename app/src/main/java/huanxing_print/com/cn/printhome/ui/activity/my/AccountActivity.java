package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderIdCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongzhiRequest;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.net.request.my.OrderIdRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountCZAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_account_record;
    private RecyclerView rv_account;
    private AccountCZAdapter adapter;
    private String rechargeAmout;
    private LinearLayout ll_xieyi;
    private String sendAmount;
    private LoadingDialog loadingDialog;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // TotleBalanceRequest.request(getSelfActivity(), new MyTotleBalanceCallBack());
    }

    private void initView() {
        loadingDialog = new LoadingDialog(getSelfActivity());
//        tv_money = (TextView) findViewById(R.id.tv_money);
//        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        rv_account = (RecyclerView) findViewById(R.id.rv_account);
        ll_xieyi = (LinearLayout) findViewById(R.id.ll_xieyi);
    }

    private void initData() {
        //充值选择列表接口
        loadingDialog.show();
        ChongzhiRequest.getChongZhi(getSelfActivity(), new MyChongzhiCallBack());
    }

    private void setListener() {
        tv_account_record.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_xieyi.setOnClickListener(this);
        findViewById(R.id.ll_wechat).setOnClickListener(this);
        findViewById(R.id.ll_alipay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wechat:
                if (ObjectUtils.isNull(rechargeAmout)) {
                    Toast.makeText(getSelfActivity(), "先选择充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                getOrderId(true);
                break;
            case R.id.ll_alipay:
                if (ObjectUtils.isNull(rechargeAmout)) {
                    Toast.makeText(getSelfActivity(), "先选择充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                getOrderId(false);
                break;
            case R.id.tv_account_record://充值记录
                startActivity(new Intent(getSelfActivity(), AccountRecordActivity.class));
                break;
            case R.id.ll_back://返回
                finishCurrentActivity();
                break;
            case R.id.ll_xieyi:
                startActivity(new Intent(getSelfActivity(), XieYiActivity.class));
                break;
        }
    }

    /**
     * 获取商品id
     * @param flag
     */
    private void getOrderId(final boolean flag) {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在加载").show();
        OrderIdRequest.getOrderId(getSelfActivity(), rechargeAmout, new OrderIdCallBack() {
            @Override
            public void success(String msg, String data) {
                DialogUtils.closeProgressDialog();
                String czOrderid = data;
                showPaySelect(czOrderid,flag);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }

    /**
     * 充值选择
     */
    private void showPaySelect(String orderid,boolean flag) {
        if (flag) {
            Go2PayRequest.go2PWeChat(getSelfActivity(), orderid, "CZ", new WeChatCallBack() {
                        @Override
                        public void success(WeChatPayBean bean) {
                            SharedPreferencesUtils.putShareValue(getSelfActivity(), "CZ", true);
                            PayUtil.getInstance(getSelfActivity()).weChatPay(bean);
                        }

                        @Override
                        public void fail(String msg) {

                        }

                        @Override
                        public void connectFail() {
                            toast("网络连接失败");
                        }
                    }
            );
        }else {
            Go2PayRequest.go2Pay(getSelfActivity(), orderid + "", "CZ", new Go2PayCallBack() {
                @Override
                public void success(String msg, String s) {
                    PayUtil.getInstance(getSelfActivity()).alipay(s);
                }

                @Override
                public void fail(String msg) {

                }

                @Override
                public void connectFail() {

                }
            });
        }
    }

    public class MyChongzhiCallBack extends ChongzhiCallBack {

        @Override
        public void success(String msg, final List<ChongZhiBean> list) {
            loadingDialog.dismiss();
            if (null!=list&&list.size()>0) {
                adapter = new AccountCZAdapter(getSelfActivity(), list);
                rv_account.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
                rv_account.setAdapter(adapter);
                adapter.setOnItemClickLitener(new AccountCZAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(ImageView view, int position) {
                        adapter.setSeclection(position);
                        adapter.notifyDataSetChanged();
                        ChongZhiBean chongZhiBean = list.get(position);
                        rechargeAmout = chongZhiBean.getRechargeAmout();
                        sendAmount = chongZhiBean.getSendAmount();

                    }
                });
            }
        }

        @Override
        public void fail(String msg) {
            loadingDialog.dismiss();
            toast(msg);
        }

        @Override
        public void connectFail() {
            loadingDialog.dismiss();
            toastConnectFail();
        }
    }
//    public class MyTotleBalanceCallBack extends TotleBalanceCallBack {
//
//        @Override
//        public void fail(String msg) {
//
//        }
//
//        @Override
//        public void connectFail() {
//
//        }
//
//        @Override
//        public void success(String msg, TotleBalanceBean bean) {
//            String totleBalance = bean.getTotleBalance();
//            tv_money.setText("￥" + totleBalance);
//        }
//    }
}
