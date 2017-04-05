package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;

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
    private ListView lv_account_record;
    private int pageNum = 1;
    private AccountRecordAdapter adapter;
    private XRefreshView xrf_czrecord;
    private boolean isLoadMore = false;
    private List<ChongZhiRecordBean.ListBean> datalist;

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
        lv_account_record = (ListView) findViewById(R.id.lv_account_record);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);

        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                datalist.clear();
                pageNum = 1;
                //获取充值记录
                ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
                xrf_czrecord.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
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

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {
            if (isLoadMore) {//如果是加载更多
                if (!ObjectUtils.isNull(bean)) {
                    xrf_czrecord.stopLoadMore();
                    if (!ObjectUtils.isNull(bean.getList())) {
                        datalist.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.doToast(getSelfActivity(), "没有更多数据");
                        return;
                    }
                } else {
                    ToastUtil.doToast(getSelfActivity(), "没有更多数据");
                    xrf_czrecord.stopLoadMore();
                    return;
                }
            } else {
                datalist = bean.getList();
                adapter = new AccountRecordAdapter(getSelfActivity(), datalist);
                lv_account_record.setAdapter(adapter);
            }
            xrf_czrecord.setPullLoadEnable(true);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_czrecord.setPinnedTime(1000);
            xrf_czrecord.setMoveForHorizontal(true);
//            xrf_czrecord.setCustomFooterView(new CustomerFooter(this));
            //设置当非RecyclerView上拉加载完成以后的回弹时间
//            xrf_czrecord.setScrollBackDuration(300);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
