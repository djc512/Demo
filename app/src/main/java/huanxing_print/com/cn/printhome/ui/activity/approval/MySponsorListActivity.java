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
import huanxing_print.com.cn.printhome.ui.adapter.MySponsorListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FailureRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.GroupRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.NormalRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.FailureRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GoneRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GroupRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.SingleRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * description: 我发起的列表
 * author LSW
 * date 2017/5/6 11:21
 * update 2017/5/6
 */
public class MySponsorListActivity extends BaseActivity {

    private ListView lv_my_list;
    private XRefreshView xrf_czrecord;
    private MySponsorListAdapter listAdapter;
    private ArrayList<ApprovalObject> datalist = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore = false;
    private SingleRedEnvelopesDialog dialog;
    private FailureRedEnvelopesDialog failureDialog;
    private GoneRedEnvelopesDialog goneRedEnvelopesDialog;
    private GroupRedEnvelopesDialog groupRedEnvelopesDialog;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_my_sponsor_list);

        init();
        functionModule();
    }


    private void init() {
        lv_my_list = (ListView) findViewById(R.id.lv_my_list);
        xrf_czrecord = (XRefreshView) findViewById(R.id.xrf_czrecord);

        //展示假的红包
//        View view = getLayoutInflater().inflate(R.layout.layout_red_package_dialog, null);
//        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getSelfActivity(),
//                R.style.CustomProgressDialog))
//                .create();
//        dialog.setView(view, 0, 0, 0, 0);
//        dialog.show();

        //展示假的红包(正常的红包)
        dialog = new SingleRedEnvelopesDialog(getSelfActivity(),
                R.style.MyDialog);
        dialog.setCancelable(false);
        dialog.setImgUrl("http://www.baidu.com");
        dialog.setRedPackageSender("张国立");
        dialog.setLeaveMsg("赶快做完这一版!!!");
        dialog.setClickListener(new NormalRedEnvelopesListener() {
            @Override
            public void open() {
                //拆红包
                ToastUtil.doToast(getSelfActivity(), "点击了拆红包!");
            }

            @Override
            public void checkDetail() {
                //查看详情
                ToastUtil.doToast(getSelfActivity(), "点击了查看详情!");
            }

            @Override
            public void closeDialog() {
                //关闭红包
                dialog.dismiss();
            }
        });
        //dialog.show();

        //失效的红包
        failureDialog = new FailureRedEnvelopesDialog(getSelfActivity(),
                R.style.MyDialog);
        failureDialog.setCancelable(false);
        failureDialog.setImgUrl("http://www.baidu.com");
        failureDialog.setRedPackageSender("张国立");
        failureDialog.setClickListener(new FailureRedEnvelopesListener() {
            @Override
            public void checkDetail() {
                //查看详情
                ToastUtil.doToast(getSelfActivity(), "点击了查看详情!");
            }

            @Override
            public void closeDialog() {
                //关闭红包
                failureDialog.dismiss();
            }
        });
        //failureDialog.show();

        //展示假的红包(抢光的红包)
        goneRedEnvelopesDialog = new GoneRedEnvelopesDialog(getSelfActivity(),
                R.style.MyDialog);
        goneRedEnvelopesDialog.setCancelable(false);
        goneRedEnvelopesDialog.setImgUrl("http://www.baidu.com");
        goneRedEnvelopesDialog.setRedPackageSender("张国立");
        goneRedEnvelopesDialog.setClickListener(new FailureRedEnvelopesListener() {

            @Override
            public void checkDetail() {
                //查看详情
                ToastUtil.doToast(getSelfActivity(), "点击了查看详情!");
            }

            @Override
            public void closeDialog() {
                //关闭红包
                goneRedEnvelopesDialog.dismiss();
            }
        });
        //goneRedEnvelopesDialog.show();

        //群红包
        groupRedEnvelopesDialog = new GroupRedEnvelopesDialog(getSelfActivity(),
                R.style.MyDialog);
        groupRedEnvelopesDialog.setCancelable(false);
        groupRedEnvelopesDialog.setImgUrl("http://www.baidu.com");
        groupRedEnvelopesDialog.setRedPackageSender("张国立");
        groupRedEnvelopesDialog.setLeaveMsg("恭喜发财,大吉大利");
        groupRedEnvelopesDialog.setMoneryNum(188.88);
        groupRedEnvelopesDialog.setClickListener(new GroupRedEnvelopesListener() {
            @Override
            public void closeDialog() {
                //关闭红包
                groupRedEnvelopesDialog.dismiss();
            }
        });
        //groupRedEnvelopesDialog.show();
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

//        ApprovalRequest.getQueryApprovalList(getSelfActivity(),
//                pageNum, pageSize, 3, baseApplication.getLoginToken(), callBack);
        xrf_czrecord.setAutoRefresh(true);
        xrf_czrecord.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                datalist.clear();
                pageNum = 1;
                //获取我发起的列表
                ApprovalRequest.getQueryApprovalList(getSelfActivity(),
                        pageNum, pageSize, 3, baseApplication.getLoginToken(), callBack);
                xrf_czrecord.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                //获取我发起的列表
                ApprovalRequest.getQueryApprovalList(getSelfActivity(), pageNum, pageSize, 3,
                        baseApplication.getLoginToken(), callBack);
            }
        });

        lv_my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //查看我的列表详情
                // ToastUtil.doToast(MySponsorListActivity.this, "查看列表详情" + position);
                Intent intent = new Intent(MySponsorListActivity.this, ApprovalBuyAddOrRemoveActivity.class);
                intent.putExtra("approveId", datalist.get(position).getId());
                startActivity(intent);
            }
        });
    }

    QueryApprovalListCallBack callBack = new QueryApprovalListCallBack() {
        @Override
        public void success(String msg, ArrayList<ApprovalObject> approvalObjects) {
            ToastUtil.doToast(getSelfActivity(), "查询我发起的列表成功");
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
                listAdapter = new MySponsorListAdapter(getSelfActivity(), datalist);
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
            ToastUtil.doToast(getSelfActivity(), "失败" + msg);
        }

        @Override
        public void connectFail() {
            ToastUtil.doToast(getSelfActivity(), "connectFail");
        }
    };
}
