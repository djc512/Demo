package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongZhiRecordRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountRecordAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private RecyclerView rv_account_record;
    private int pageNum = 1;
    private AccountRecordAdapter adapter;
    private XRefreshView xrf_czrecord;
    private List<ChongZhiRecordBean.DataBean.ListBean> dataList;

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
        ChongZhiRecordRequest.getCzRecord(getSelfActivity(),pageNum,new MyChongzhiRecordCallBack());

        rv_account_record.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountRecordAdapter(getSelfActivity(),dataList);
        rv_account_record.setAdapter(adapter);

        xrf_czrecord.setPinnedTime(1000);
        xrf_czrecord.setMoveForHorizontal(true);
        xrf_czrecord.setPullLoadEnable(true);
        xrf_czrecord.setAutoLoadMore(false);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xrf_czrecord.enableReleaseToLoadMore(true);
        xrf_czrecord.enableRecyclerViewPullUp(true);
        xrf_czrecord.enablePullUpWhenLoadCompleted(true);
    }

    private void initView() {
        xrf_czrecord = (XRefreshView) findViewById(R.id.xrf_czrecord);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_account_record = (RecyclerView) findViewById(R.id.rv_account_record);
    }
    private void setListener() {
        ll_back.setOnClickListener(this);

        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){

            @Override
            public void onRefresh() {
                super.onRefresh();
//                pageNum = 1;
//                //获取充值记录
//                ChongZhiRecordRequest.getCzRecord(getSelfActivity(),pageNum,new MyChongzhiRecordCallBack());
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
//
//                pageNum++;
//                dataList.addAll(dataList);
//                adapter.notifyDataSetChanged();
//
//                xrf_czrecord.setLoadComplete(true);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private class MyChongzhiRecordCallBack extends ChongZhiRecordCallBack{

        private int countX;

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {
            ChongZhiRecordBean.DataBean data = bean.getData();
            //返回的条目列表个数
            countX = data.getCountX();
            //数据的列表
            dataList = data.getList();

            toast("请求成功");
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
