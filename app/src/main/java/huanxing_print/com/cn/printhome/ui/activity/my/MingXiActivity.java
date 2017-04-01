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
import huanxing_print.com.cn.printhome.model.my.MingxiDetailBean;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.MingXiDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2DebitRequest;
import huanxing_print.com.cn.printhome.net.request.my.MingXiDetailRequest;
import huanxing_print.com.cn.printhome.ui.adapter.MyBillAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class MingXiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private MyBillAdapter adapter;
    private RecyclerView rv_bill_detail;
    private TextView tv_bill_debit;
    private XRefreshView xrf_zdmx;
    private List<MingxiDetailBean.ListBean> listAll = new ArrayList<>();

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
    private int pageNum = 1;
    private void initData() {
        //获取账单明细
        MingXiDetailRequest.getMxDetail(getSelfActivity(),pageNum,new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_bill_debit.setOnClickListener(this);

        xrf_zdmx.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                listAll.clear();
                pageNum = 1;
                MingXiDetailRequest.getMxDetail(getSelfActivity(),pageNum,new MyCallBack());
                xrf_zdmx.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                pageNum++;
                MingXiDetailRequest.getMxDetail(getSelfActivity(),pageNum,new MyCallBack());
                xrf_zdmx.stopLoadMore();
            }
        });
    }

    private void initView() {
        xrf_zdmx = (XRefreshView) findViewById(R.id.xrf_zdmx);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_bill_detail = (RecyclerView) findViewById(R.id.rv_bill_detail);
        tv_bill_debit = (TextView) findViewById(R.id.tv_bill_debit);
    }
    private String billValue;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.tv_bill_debit://开发票
                //根据后台判断能否开发票
                Go2DebitRequest.getRequset(getSelfActivity(), new Go2PayCallBack() {
                    @Override
                    public void success(String msg, String s) {
                        billValue = s;
                        if(billValue.equals("0")){
                            ToastUtil.doToast(getSelfActivity(),"可开发票金额为0");
                            return;
                        }else {
                            Intent intent = new Intent(getSelfActivity(),BillDebitActivity.class);
                            intent.putExtra("billValue",billValue);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void fail(String msg) {

                    }

                    @Override
                    public void connectFail() {

                    }
                });
                break;
        }
    }

    public class MyCallBack extends MingXiDetailCallBack{

        @Override
        public void success(String msg, MingxiDetailBean bean) {
            List<MingxiDetailBean.ListBean> list = bean.getList();

            if (!ObjectUtils.isNull(list)) {
                listAll.addAll(list);
            } else {
                ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                xrf_zdmx.stopLoadMore();
                return;
            }
            LinearLayoutManager manager = new LinearLayoutManager(getSelfActivity());
            adapter = new MyBillAdapter(getSelfActivity(),listAll);
            rv_bill_detail.setLayoutManager(manager);
            rv_bill_detail.setAdapter(adapter);
            manager.scrollToPositionWithOffset(listAll.size() -1,0);
            xrf_zdmx.setPinnedTime(1000);
            xrf_zdmx.setMoveForHorizontal(true);
            xrf_zdmx.setPullLoadEnable(true);
            xrf_zdmx.setAutoLoadMore(false);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(getSelfActivity()));
            xrf_zdmx.enableReleaseToLoadMore(true);
            xrf_zdmx.enableRecyclerViewPullUp(true);
            xrf_zdmx.enablePullUpWhenLoadCompleted(true);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }

}
