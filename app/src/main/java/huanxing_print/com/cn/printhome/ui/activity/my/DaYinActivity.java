package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
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
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.net.callback.my.DaYinListCallBack;
import huanxing_print.com.cn.printhome.net.request.my.DaYinListRequest;
import huanxing_print.com.cn.printhome.ui.adapter.DingDanListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class DaYinActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RecyclerView rv_dingdan;
    private XRefreshView xrf_dingdan;
    private DingDanListAdapter adapter;
    private List<DaYinListBean.DataBean.ListBean> list;

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

    private void setListener() {
        ll_back.setOnClickListener(this);

        adapter.setOnItemClickLitener(new DingDanListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getSelfActivity(),OrderDetailActivity.class);
                startActivity(intent);
            }
        });


        xrf_dingdan.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            //刷新
            @Override
            public void onRefresh() {
                super.onRefresh();
            }

            //加载更多
            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });
    }

    private void initData() {
        //获取数据
        DaYinListRequest.getDaYinList(getSelfActivity(),1,new MyCallBack());


        adapter = new DingDanListAdapter(this,null);
        rv_dingdan.setLayoutManager(new LinearLayoutManager(this));
        rv_dingdan.setAdapter(adapter);

        // 设置静默加载模式
        // xRefreshView1.setSilenceLoadMore();
        // 静默加载模式不能设置footerview
        //设置刷新完成以后，headerview固定的时间
        xrf_dingdan.setPinnedTime(1000);
        xrf_dingdan.setMoveForHorizontal(true);
        xrf_dingdan.setPullLoadEnable(true);
        xrf_dingdan.setAutoLoadMore(false);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xrf_dingdan.enableReleaseToLoadMore(true);
        xrf_dingdan.enableRecyclerViewPullUp(true);
        xrf_dingdan.enablePullUpWhenLoadCompleted(true);
        //设置静默加载时提前加载的item个数
        //xRefreshView1.setPreLoadCount(4);
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
                finish();
                break;
        }
    }

    public class MyCallBack extends DaYinListCallBack{

        @Override
        public void success(String msg, DaYinListBean bean) {
            DaYinListBean.DataBean data = bean.getData();
            list = data.getList();
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
