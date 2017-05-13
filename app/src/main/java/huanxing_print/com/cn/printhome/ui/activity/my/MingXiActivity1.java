package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;

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
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class MingXiActivity1 extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private MyBillAdapter adapter;
    private ListView lv_bill_detail;
    private TextView tv_bill_debit;
    private XRefreshView xrf_zdmx;
    private List<MingxiDetailBean.ListBean> list;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_user_mingxi);
        initView();
        initData();
        setListener();
    }

    private int pageNum = 1;

    private void initData() {
        //获取账单明细
        MingXiDetailRequest.getMxDetail(getSelfActivity(), pageNum, new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_bill_debit.setOnClickListener(this);

        xrf_zdmx.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                list.clear();
                pageNum = 1;
                MingXiDetailRequest.getMxDetail(getSelfActivity(), pageNum, new MyCallBack());
                xrf_zdmx.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                MingXiDetailRequest.getMxDetail(getSelfActivity(), pageNum, new MyCallBack());
            }
        });
    }

    private void initView() {
        xrf_zdmx = (XRefreshView) findViewById(R.id.xrf_zdmx);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lv_bill_detail = (ListView) findViewById(R.id.lv_bill_detail);
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
                        if (billValue.equals("0")) {
                            ToastUtil.doToast(getSelfActivity(), "可开发票金额为0");
                            return;
                        } else {
//                            Intent intent = new Intent(getSelfActivity(), BillDebitActivity.class);
//                            intent.putExtra("billValue", billValue);
//                            startActivity(intent);
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

    private boolean isLoadMore = false;

    public class MyCallBack extends MingXiDetailCallBack {

        @Override
        public void success(String msg, MingxiDetailBean bean) {

            if (isLoadMore) {
                xrf_zdmx.stopLoadMore();
                if (ObjectUtils.isNull(bean)) {
                    if (!ObjectUtils.isNull(bean.getList())) {
                        list.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastUtil.doToast(getSelfActivity(), "没有更多数据");
                        return;
                    }
                } else {
                    ToastUtil.doToast(getSelfActivity(), "没有更多数据");
                    xrf_zdmx.stopLoadMore();
                    return;
                }
            } else {
                list = bean.getList();
                adapter = new MyBillAdapter(getSelfActivity(), list);
                lv_bill_detail.setAdapter(adapter);
            }

            if (!ObjectUtils.isNull(list)) {
                list.addAll(list);
            } else {
                ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                xrf_zdmx.stopLoadMore();
                return;
            }
            xrf_zdmx.setPullLoadEnable(true);
            xrf_zdmx.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_zdmx.setPinnedTime(1000);
            xrf_zdmx.setCustomFooterView(new CustomerFooter(getSelfActivity()));
            xrf_zdmx.setMoveForHorizontal(true);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }

}
