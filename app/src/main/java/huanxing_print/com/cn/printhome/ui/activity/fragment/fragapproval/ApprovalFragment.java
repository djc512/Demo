package huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalListCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalApplyDetailsActivity;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalBuyAddOrRemoveActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalListAdapter;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalFragment extends Fragment implements ListView.OnItemClickListener {


    private ApprovalListAdapter lvAdapter;
    private XRefreshView xrf_czrecord;
    private ListView lv_my_list;
    private ArrayList<ApprovalObject> datalist = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore = false;
    private String token;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_approval, null);
        initView(view);
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        EventBus.getDefault().register(this);
        intiData();
        initListener();

        return view;

    }

    private void initListener() {
        lv_my_list.setOnItemClickListener(this);
    }

    private void intiData() {
        xrf_czrecord.startRefresh();
        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                datalist.clear();
                pageNum = 1;
                //查询审批的列表
                ApprovalRequest.getQueryApprovalList(getActivity(),
                        pageNum, pageSize, 1, token, callBack);
                xrf_czrecord.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                //获取我发起的列表
                ApprovalRequest.getQueryApprovalList(getActivity(), pageNum, pageSize, 1,
                        token, callBack);
            }
        });
    }


    private void initView(View view) {
        lv_my_list = (ListView) view.findViewById(R.id.lv_my_list);
        xrf_czrecord = (XRefreshView) view.findViewById(R.id.xrf_czrecord);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Subscriber(tag = "refreshApprovalNum")
    private void setRefreshApprovalList() {
        intiData();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (1 == datalist.get(position).getType()) {
            intent.setClass(getActivity(), ApprovalBuyAddOrRemoveActivity.class);
        } else if (2 == datalist.get(position).getType()) {
            intent.setClass(getActivity(), ApprovalApplyDetailsActivity.class);
        }
        intent.putExtra("approveId", datalist.get(position).getId());
        startActivity(intent);
    }

    QueryApprovalListCallBack callBack = new QueryApprovalListCallBack() {
        @Override
        public void success(String msg, ArrayList<ApprovalObject> approvalObjects) {
            //ToastUtil.doToast(getSelfActivity(), "查询我发起的列表成功");
            if (isLoadMore) {//如果是加载更多
                if (null!=approvalObjects&&approvalObjects.size()>0) {
                    xrf_czrecord.stopLoadMore();
                    datalist.addAll(approvalObjects);
                    lvAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.doToast(getActivity(), "没有更多数据");
                    xrf_czrecord.stopLoadMore();
                    return;
                }
            } else {
                if (null!=approvalObjects&&approvalObjects.size()>0) {
                    datalist = approvalObjects;
                    lvAdapter = new ApprovalListAdapter(getActivity(), datalist);
                    lv_my_list.setAdapter(lvAdapter);
                } else {
                   // ToastUtil.doToast(getActivity(), "暂无数据");
                }
            }
            xrf_czrecord.setPullLoadEnable(true);
            xrf_czrecord.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_czrecord.setPinnedTime(1000);
            xrf_czrecord.setMoveForHorizontal(true);
            xrf_czrecord.setCustomFooterView(new CustomerFooter(getActivity()));
        }

        @Override
        public void fail(String msg) {
            ToastUtil.doToast(getActivity(), "失败" + msg);
        }

        @Override
        public void connectFail() {
            ToastUtil.doToast(getActivity(), "网络连接失败");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
