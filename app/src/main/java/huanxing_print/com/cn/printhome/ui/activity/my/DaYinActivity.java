package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;

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
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class DaYinActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private ListView lv_dingdan;
    private XRefreshView xrf_dingdan;
    private DingDanListAdapter adapter;
    private boolean isLoadMore = false;
    private List<DaYinListBean.ListBean> list;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_user_dayin);
        CommonUtils.initSystemBar(this);
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
                isLoadMore = false;
                list.clear();
                pageNum = 1;
                DaYinListRequest.getDaYinList(getSelfActivity(), pageNum, new MyCallBack());
                xrf_dingdan.stopRefresh();
            }

            //加载更多
            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                DaYinListRequest.getDaYinList(getSelfActivity(), pageNum, new MyCallBack());
            }
        });

        lv_dingdan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                int id = list.get(position).getId();
                Intent intent = new Intent(getSelfActivity(), OrderDetailActivity.class);
                intent.putExtra("id",String.valueOf(id));
                startActivity(intent);
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
        lv_dingdan = (ListView) findViewById(R.id.lv_dingdan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
    public class MyCallBack extends DaYinListCallBack {

        @Override
        public void success(String msg, DaYinListBean bean) {
            LinearLayoutManager manager = new LinearLayoutManager(getSelfActivity());
            if (isLoadMore) {
                if (!ObjectUtils.isNull(bean)) {
                    xrf_dingdan.stopLoadMore();
                    if (!ObjectUtils.isNull(bean.getList())) {
                        list.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastUtil.doToast(getSelfActivity(),"没有更多数据");
                        return;
                    }
                }else {
                    ToastUtil.doToast(getSelfActivity(),"没有更多数据");
                    xrf_dingdan.stopLoadMore();
                    return;
                }
            }else {
                list = bean.getList();
                adapter = new DingDanListAdapter(getSelfActivity(), list);
                lv_dingdan.setAdapter(adapter);
            }
            xrf_dingdan.setPullLoadEnable(true);
            xrf_dingdan.setAutoLoadMore(false);
            xrf_dingdan.setPinnedTime(1000);
            xrf_dingdan.setMoveForHorizontal(true);
            xrf_dingdan.setCustomFooterView(new CustomerFooter(getSelfActivity()));
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
