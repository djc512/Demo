package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongZhiRecordRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountRecordAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private RecyclerView rv_account_record;
    private int pageNum = 1;
    private AccountRecordAdapter adapter;
    private XRefreshView xrf_czrecord;
    private List<ChongZhiRecordBean.ListBean> list = new ArrayList<>();
    private LinearLayoutManager manager;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_account_record);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        //获取充值记录
        ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
    }

    private void initView() {
        xrf_czrecord = (XRefreshView) findViewById(R.id.xrf_czrecord);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_account_record = (RecyclerView) findViewById(R.id.rv_account_record);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);

        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                pageNum = 1;
//                //获取充值记录
                ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
                xrf_czrecord.stopRefresh();
                manager.scrollToPosition(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                pageNum++;
                ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
                xrf_czrecord.stopLoadMore();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private class MyChongzhiRecordCallBack extends ChongZhiRecordCallBack {

        private int countX;

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {
            List<ChongZhiRecordBean.ListBean> datalist = bean.getList();
            if (!ObjectUtils.isNull(datalist)) {
                if (datalist.size() > list.size()) {//获取最新的数据
                    list.addAll(0, datalist);
                } else {
                    ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                }
            } else {
                ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                return;
            }
            manager = new LinearLayoutManager(getSelfActivity());

            rv_account_record.setLayoutManager(manager);
            manager.scrollToPosition(0);
            adapter = new AccountRecordAdapter(getSelfActivity(), list);
            manager.scrollToPosition(0);
            rv_account_record.setAdapter(adapter);

            xrf_czrecord.setPinnedTime(1000);
            xrf_czrecord.setMoveForHorizontal(true);
            xrf_czrecord.setPullLoadEnable(true);
            xrf_czrecord.setAutoLoadMore(false);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(getSelfActivity()));
            xrf_czrecord.enableReleaseToLoadMore(true);
            xrf_czrecord.enableRecyclerViewPullUp(true);
            xrf_czrecord.enablePullUpWhenLoadCompleted(true);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
