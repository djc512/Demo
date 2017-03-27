package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.MyBillAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class MingXiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private MyBillAdapter adapter;
    private RecyclerView rv_bill_detail;
    private TextView tv_bill_debit;
    private XRefreshView xrf_zdmx;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_mingxi);
        initView();
        initData();
        setListener();
    }

    private List<String> list = new ArrayList<>();

    private void initData() {
        //获取账单明细

        adapter = new MyBillAdapter(getSelfActivity());
        rv_bill_detail.setLayoutManager(new LinearLayoutManager(this));
        rv_bill_detail.setAdapter(adapter);

        xrf_zdmx.setPinnedTime(1000);
        xrf_zdmx.setMoveForHorizontal(true);
        xrf_zdmx.setPullLoadEnable(true);
        xrf_zdmx.setAutoLoadMore(false);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xrf_zdmx.enableReleaseToLoadMore(true);
        xrf_zdmx.enableRecyclerViewPullUp(true);
        xrf_zdmx.enablePullUpWhenLoadCompleted(true);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_bill_debit.setOnClickListener(this);

    }

    private void initView() {
        xrf_zdmx = (XRefreshView) findViewById(R.id.xrf_zdmx);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_bill_detail = (RecyclerView) findViewById(R.id.rv_bill_detail);
        tv_bill_debit = (TextView) findViewById(R.id.tv_bill_debit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_bill_debit://开发票
                startActivity(new Intent(getSelfActivity(),BillDebitActivity.class));
                break;
        }
    }
}
