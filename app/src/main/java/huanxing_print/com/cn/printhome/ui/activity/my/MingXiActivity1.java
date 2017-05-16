package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.OrderListBean;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderListCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2DebitRequest;
import huanxing_print.com.cn.printhome.net.request.my.OrderListRequest;
import huanxing_print.com.cn.printhome.ui.adapter.MyBillAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class MingXiActivity1 extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private ListView lv_bill_detail;
    private TextView tv_bill_debit;
    private XRefreshView xrf_zdmx;
    private List<OrderListBean> datalist;
    private MyBillAdapter adapter;

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
        DialogUtils.showProgressDialog(getSelfActivity(), "正在加载...").show();
        OrderListRequest.request(getSelfActivity(), "1", new MyCallBack());
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_bill_debit.setOnClickListener(this);

        xrf_zdmx.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                if (null == datalist) {
                    Toast.makeText(getSelfActivity(), "没有充值记录", Toast.LENGTH_SHORT).show();
                    xrf_zdmx.stopRefresh();
                    return;
                }
                datalist.clear();
                pageNum = 1;
                //获取充值记录
                OrderListRequest.request(getSelfActivity(), pageNum + "", new MyCallBack());
                xrf_zdmx.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                OrderListRequest.request(getSelfActivity(), pageNum + "", new MyCallBack());
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
                            Intent intent = new Intent(getSelfActivity(), ReceiptNewActivity.class);
                            intent.putExtra("billValue", s);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        xrf_zdmx.stopLoadMore();
                        xrf_zdmx.stopRefresh();
                    }

                    @Override
                    public void connectFail() {

                    }
                });
                break;
        }
    }

    private boolean isLoadMore = false;

    public class MyCallBack extends OrderListCallBack {

        @Override
        public void success(List<OrderListBean> list) {
            DialogUtils.closeProgressDialog();
            if (isLoadMore) {//如果是加载更多
                if (!ObjectUtils.isNull(list)) {
                    xrf_zdmx.stopLoadMore();
                    datalist.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.doToast(getSelfActivity(), "没有更多数据");
                    xrf_zdmx.stopLoadMore();
                    return;
                }
            } else {
                if (null != list) {
                    datalist = list;
                    if (null != datalist && datalist.size() > 0) {
                        adapter = new MyBillAdapter(getSelfActivity(), datalist);
                        lv_bill_detail.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getSelfActivity(), "没有充值数据", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            xrf_zdmx.setPullLoadEnable(true);
            xrf_zdmx.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_zdmx.setPinnedTime(1000);
            xrf_zdmx.setMoveForHorizontal(true);
            xrf_zdmx.setCustomFooterView(new CustomerFooter(getSelfActivity()));
            //设置当非RecyclerView上拉加载完成以后的回弹时间
            // xrf_zdmx.setScrollBackDuration(300);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }

}
