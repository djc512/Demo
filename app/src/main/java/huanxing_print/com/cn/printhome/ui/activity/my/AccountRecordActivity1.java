package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongZhiRecordRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountRecordAdapter1;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordActivity1 extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private RecyclerView lv_account_record;
    private int pageNum = 1;
    private AccountRecordAdapter1 adapter;
    private XRefreshView xrf_czrecord;
    private boolean isLoadMore = false;
    private List<ChongZhiRecordBean.ListBean> datalist;
    private TextView tv_receipt;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_account_record1);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        AccountRecordAdapter1 adapter = new AccountRecordAdapter1(getSelfActivity());
        lv_account_record.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
        lv_account_record.setAdapter(adapter);

        //获取充值记录
        ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
    }

    private void initView() {
        tv_receipt = (TextView) findViewById(R.id.tv_receipt);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lv_account_record = (RecyclerView) findViewById(R.id.lv_account_record);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_receipt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.tv_receipt://开票
                startActivity(new Intent(getSelfActivity(), ReceiptActivity.class));
                break;
        }
    }
    private class MyChongzhiRecordCallBack extends ChongZhiRecordCallBack {

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {

        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
