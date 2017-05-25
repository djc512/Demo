package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongZhiRecordRequest;
import huanxing_print.com.cn.printhome.net.request.my.Go2DebitRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountRecordAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

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
    private TextView tv_receipt;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_account_record);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在加载").show();
        //获取充值记录
        ChongZhiRecordRequest.getCzRecord(getSelfActivity(), pageNum, new MyChongzhiRecordCallBack());
        getReceiptAccount();
    }

    private void initView() {
        tv_receipt = (TextView) findViewById(R.id.tv_receipt);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lv_account_record = (ListView) findViewById(R.id.lv_account_record);
        xrf_czrecord = (XRefreshView) findViewById(R.id.xrf_czrecord);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        tv_receipt.setOnClickListener(this);

        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                if (null == datalist) {
                    Toast.makeText(getSelfActivity(), "没有充值记录", Toast.LENGTH_SHORT).show();
                    xrf_czrecord.stopRefresh();
                    return;
                }
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
                finishCurrentActivity();
                break;
            case R.id.tv_receipt://开票
                if ("0.00".equals(money)) {
                    Toast.makeText(getSelfActivity(), "可开发票为0", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getSelfActivity(), ReceiptNewActivity.class);
                    intent.putExtra("billValue", money);
                    startActivity(intent);
                }
                break;
        }
    }

    private String money;

    /**
     * 获取发票金额
     */
    private void getReceiptAccount() {
        //根据后台判断能否开发票
        Go2DebitRequest.getRequset(getSelfActivity(), new Go2PayCallBack() {
            @Override
            public void success(String msg, String s) {
                money = s;
                DialogUtils.closeProgressDialog();
                if ("0.00".equals(s)) {
                    tv_receipt.setTextColor(getResources().getColor(R.color.gray8));
                } else {
                    tv_receipt.setTextColor(getResources().getColor(R.color.yellow2));
                }
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }

    private class MyChongzhiRecordCallBack extends ChongZhiRecordCallBack {

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {
            DialogUtils.closeProgressDialog();
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
                if (bean != null) {
                    datalist = bean.getList();
                    if (null != datalist && datalist.size() > 0) {
                        AccountRecordAdapter adapter = new AccountRecordAdapter(getSelfActivity(), datalist);

                        lv_account_record.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getSelfActivity(), "没有充值数据", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            xrf_czrecord.setAutoRefresh(true);//自动刷新
            xrf_czrecord.setPullLoadEnable(true);
            xrf_czrecord.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_czrecord.setPinnedTime(1000);
            xrf_czrecord.setMoveForHorizontal(true);
            xrf_czrecord.setCustomFooterView(new CustomerFooter(getSelfActivity()));
            //设置当非RecyclerView上拉加载完成以后的回弹时间
            // xrf_czrecord.setScrollBackDuration(300);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {
            toast("网络连接错误");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
