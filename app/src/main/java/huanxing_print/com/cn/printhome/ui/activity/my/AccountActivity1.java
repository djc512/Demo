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
import huanxing_print.com.cn.printhome.ui.adapter.AccountCZAdapter1;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity1 extends BaseActivity implements View.OnClickListener {

    private TextView tv_money;
    private Button btn_chongzhi;
    private LinearLayout ll_back;
    private TextView tv_account_record;
    private RecyclerView rv_account;
    private AccountCZAdapter1 adapter;

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
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        rv_account = (RecyclerView) findViewById(R.id.rv_account);
    }

    private void initData() {
        adapter = new AccountCZAdapter1(getSelfActivity(), null);
        rv_account.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        rv_account.setAdapter(adapter);

        //充值接口
        ChongzhiRequest.getChongZhi(getSelfActivity(), new MyChongzhiCallBack());
    }

    private void setListener() {
        tv_account_record.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_chongzhi.setOnClickListener(this);

        adapter.setOnItemClickLitener(new AccountCZAdapter1.OnItemClickLitener() {
            @Override
            public void onItemClick(ImageView view, int position) {
                adapter.setSeclection(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chongzhi:
                Toast.makeText(getSelfActivity(), "先选择充值金额", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getSelfActivity(), PayActivity1.class));
                break;
            case R.id.tv_account_record://充值记录
                startActivity(new Intent(getSelfActivity(), AccountRecordActivity1.class));
                break;
            case R.id.ll_back://返回
                finishCurrentActivity();
                break;
        }
    }
    public class MyChongzhiCallBack extends ChongzhiCallBack {

        @Override
        public void success(String msg, List<ChongZhiBean> list) {
            adapter.updateData(list);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
