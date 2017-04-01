package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
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
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.net.callback.my.DaYinListCallBack;
import huanxing_print.com.cn.printhome.net.request.my.DaYinListRequest;
import huanxing_print.com.cn.printhome.ui.adapter.DingDanListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class DaYinActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RecyclerView rv_dingdan;
    private XRefreshView xrf_dingdan;
    private DingDanListAdapter adapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activty_user_dayin);
        initView();
        initData();
        setListener();
    }
    private int pageNum = 1;
    private void setListener() {
        ll_back.setOnClickListener(this);
        xrf_dingdan.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            //刷新
            @Override
            public void onRefresh() {
                super.onRefresh();
                pageNum = 1;
                DaYinListRequest.getDaYinList(getSelfActivity(), pageNum, new MyCallBack());
                xrf_dingdan.stopRefresh();
            }

            //加载更多
            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                pageNum++;
                DaYinListRequest.getDaYinList(getSelfActivity(), pageNum, new MyCallBack());
                xrf_dingdan.stopLoadMore();
            }
        });
    }

    private void initData() {
        //获取数据
        DaYinListRequest.getDaYinList(getSelfActivity(), pageNum, new MyCallBack());
    }

    private void initView() {
        xrf_dingdan = (XRefreshView) findViewById(R.id.xrf_dingdan);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_dingdan = (RecyclerView) findViewById(R.id.rv_dingdan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
    private  List<DaYinListBean.ListBean> listAll = new ArrayList<>();
    public class MyCallBack extends DaYinListCallBack {

        @Override
        public void success(String msg, DaYinListBean bean) {
            final List<DaYinListBean.ListBean> list = bean.getList();

            if (!ObjectUtils.isNull(list)) {
                if (list.size() > listAll.size()) {//获取最新的数据
                    listAll.addAll(0, list);
                } else {
                    ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                }
            } else {
                ToastUtil.doToast(getSelfActivity(), "已经是最新数据了");
                return;
            }

            adapter = new DingDanListAdapter(getSelfActivity(), listAll);
            rv_dingdan.setLayoutManager(new LinearLayoutManager(getSelfActivity()));
            rv_dingdan.setAdapter(adapter);

            // 设置静默加载模式
            // xRefreshView1.setSilenceLoadMore();
            // 静默加载模式不能设置footerview
            //设置刷新完成以后，headerview固定的时间
            xrf_dingdan.setPinnedTime(1000);
            xrf_dingdan.setMoveForHorizontal(true);
            xrf_dingdan.setPullLoadEnable(true);
            xrf_dingdan.setAutoLoadMore(false);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(getSelfActivity()));
            xrf_dingdan.enableReleaseToLoadMore(true);
            xrf_dingdan.enableRecyclerViewPullUp(true);
            xrf_dingdan.enablePullUpWhenLoadCompleted(true);
            //设置静默加载时提前加载的item个数
            //xRefreshView1.setPreLoadCount(4);

            adapter.setOnItemClickLitener(new DingDanListAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(int position) {
                    int id = listAll.get(position).getId();

                    Intent intent = new Intent(getSelfActivity(), OrderDetailActivity.class);
                    intent.putExtra("id",String.valueOf(id));
                    startActivity(intent);
                }
            });
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
