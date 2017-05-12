package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalListCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.adapter.CopyToMeAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * description: 抄送给我的
 * author LSW
 * date 2017/5/6 14:21
 * update 2017/5/6
 */
public class CopyToMeActivity extends BaseActivity {

    private ListView lv_my_list;
    private XRefreshView xrf_czrecord;
    private ArrayList<ApprovalObject> datalist = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore = false;
    private CopyToMeAdapter listAdapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_copy_to_me);

        init();
        functionModule();
    }

    private void init() {
        lv_my_list = (ListView) findViewById(R.id.lv_my_list);
        xrf_czrecord = (XRefreshView) findViewById(R.id.xrf_czrecord);
    }

    private void functionModule() {

        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ApprovalRequest.getQueryApprovalList(getSelfActivity(),
                pageNum, pageSize, 4, baseApplication.getLoginToken(), callBack);

        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                datalist.clear();
                pageNum = 1;
                //获取我发起的列表
                ApprovalRequest.getQueryApprovalList(getSelfActivity(), pageNum, pageSize, 3,
                        baseApplication.getLoginToken(), callBack);
                xrf_czrecord.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                //获取我发起的列表
                ApprovalRequest.getQueryApprovalList(getSelfActivity(), pageNum, pageSize, 4,
                        baseApplication.getLoginToken(), callBack);
            }
        });

        listAdapter = new CopyToMeAdapter(getSelfActivity(), datalist);
        lv_my_list.setAdapter(listAdapter);
        lv_my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (1 == datalist.get(position).getType()) {
                    intent.setClass(CopyToMeActivity.this, ApprovalBuyAddOrRemoveActivity.class);
                } else if (2 == datalist.get(position).getType()) {
                    intent.setClass(CopyToMeActivity.this, ApprovalApplyDetailsActivity.class);
                }
                intent.putExtra("approveId", datalist.get(position).getId());
                startActivity(intent);
            }
        });
    }

    QueryApprovalListCallBack callBack = new QueryApprovalListCallBack() {
        @Override
        public void success(String msg, ArrayList<ApprovalObject> approvalObjects) {
            //ToastUtil.doToast(getSelfActivity(), "查询抄送给我的列表成功");
            if (isLoadMore) {//如果是加载更多
                if (!ObjectUtils.isNull(approvalObjects)) {
                    xrf_czrecord.stopLoadMore();
                    if (!ObjectUtils.isNull(approvalObjects.size())) {
                        datalist.addAll(approvalObjects);
                        listAdapter.notifyDataSetChanged();
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
                datalist = approvalObjects;
                listAdapter = new CopyToMeAdapter(getSelfActivity(), datalist);
                lv_my_list.setAdapter(listAdapter);
            }
            xrf_czrecord.setPullLoadEnable(true);
            xrf_czrecord.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_czrecord.setPinnedTime(1000);
            xrf_czrecord.setMoveForHorizontal(true);
            xrf_czrecord.setCustomFooterView(new CustomerFooter(getSelfActivity()));
        }

        @Override
        public void fail(String msg) {
            ToastUtil.doToast(getSelfActivity(), msg);
        }

        @Override
        public void connectFail() {
            ToastUtil.doToast(getSelfActivity(), "连接失败!");
        }
    };
}
