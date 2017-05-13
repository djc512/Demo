package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongzhiRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountCZAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

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
    private String totleBalance;
    private String rechargeAmout;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_user_account);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        totleBalance = getIntent().getStringExtra("totleBalance");
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        rv_account = (RecyclerView) findViewById(R.id.rv_account);
    }

    private void initData() {

        if (null != totleBalance) {
            tv_money.setText("￥:" + totleBalance);
        }
        //充值接口
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
                if (null == rechargeAmout) {
                    Toast.makeText(getSelfActivity(), "先选择充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(baseApplication, rechargeAmout + "", Toast.LENGTH_SHORT).show();
                Intent payIntent = new Intent(getSelfActivity(), PayActivity.class);
                payIntent.putExtra("rechargeAmout", rechargeAmout);
                startActivity(payIntent);
                break;
            case R.id.tv_account_record://充值记录
                startActivity(new Intent(getSelfActivity(), AccountRecordActivity.class));
                break;
            case R.id.ll_back://返回
                finishCurrentActivity();
                break;
        }
    }

    public class MyChongzhiCallBack extends ChongzhiCallBack {

        @Override
        public void success(String msg, final List<ChongZhiBean> list) {
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
