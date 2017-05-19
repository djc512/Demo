package huanxing_print.com.cn.printhome.ui.activity.yinxin;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.chat.CommonPackage;
import huanxing_print.com.cn.printhome.model.chat.GroupLuckyPackageDetail;
import huanxing_print.com.cn.printhome.model.chat.RedPackageDetail;
import huanxing_print.com.cn.printhome.model.yinxin.RedPackageBean;
import huanxing_print.com.cn.printhome.net.callback.chat.GetCommonPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetLuckyPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.PackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.ui.adapter.GroupCommonRedPackageAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.GroupLuckyRedPackageAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.SingleRedPackageAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class RedPackageRecordActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recordRecView;
    private SingleRedPackageAdapter singleAdapter;
    private GroupCommonRedPackageAdapter commonAdapter;
    private GroupLuckyRedPackageAdapter groupLuckyRedPackageAdapter;
    private boolean singleType;
    private TextView tv_instructions;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        CommonUtils.initSystemBar(this, R.color.red_package_red);
        setContentView(R.layout.activity_red_package_record);
        initView();
    }

    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initView() {
        recordRecView = (RecyclerView) findViewById(R.id.recordRecView);
        tv_instructions= (TextView) findViewById(R.id.tv_instructions);
        findViewById(R.id.exitTv).setOnClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recordRecView.setLayoutManager(mLayoutManager);
        recordRecView.setHasFixedSize(true);
        recordRecView.setItemAnimator(new DefaultItemAnimator());
        recordRecView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(this, R.color.devide_gray)));
        RedPackageBean date = new RedPackageBean();
        String easemobGroupId = getIntent().getStringExtra("easemobGroupId");
        String groupId = getIntent().getStringExtra("groupId");
        String packetId = getIntent().getStringExtra("packetId");
        int type = getIntent().getIntExtra("type", -1);
        singleType = getIntent().getBooleanExtra("singleType", false);
        Log.d("CMCC", "easemobGroupId:" + easemobGroupId + ",groupId:" + groupId + ",packetId:" + packetId +
                ",type:" + type + "," + singleType);

        if (singleType) {
            tv_instructions.setVisibility(View.VISIBLE);
            //单聊红包
            ChatRequest.queryPackageDetail(getSelfActivity(), baseApplication.getLoginToken(),
                    packetId, detailCallBack);
        } else {
            tv_instructions.setVisibility(View.GONE);
            if (1 == type) {
                //群普通红包
                ChatRequest.getCommonPackageDetail(getSelfActivity(), baseApplication.getLoginToken(),
                        easemobGroupId, groupId, packetId, callBack);
            } else if (2 == type) {
                //群拼手气红包
                DialogUtils.showProgressDialog(this, "加载中").show();
                ChatRequest.getLuckyPackageDetail(getSelfActivity(), baseApplication.getLoginToken(),
                        easemobGroupId, groupId, packetId, luckyCallBack);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitTv:
                finishCurrentActivity();
                break;
            default:
                break;
        }
    }

    GetCommonPackageDetailCallBack callBack = new GetCommonPackageDetailCallBack() {
        @Override
        public void success(String msg, CommonPackage detail) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
            commonAdapter = new GroupCommonRedPackageAdapter(getSelfActivity(), detail);
            recordRecView.setAdapter(commonAdapter);
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };

    GetLuckyPackageDetailCallBack luckyCallBack = new GetLuckyPackageDetailCallBack() {
        @Override
        public void success(String msg, GroupLuckyPackageDetail detail) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
            if (null != detail) {
                Log.d("CMCC", "解析成功:" + detail.getList().size());
                groupLuckyRedPackageAdapter = new GroupLuckyRedPackageAdapter(getSelfActivity(), detail);
                recordRecView.setAdapter(groupLuckyRedPackageAdapter);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };

    PackageDetailCallBack detailCallBack = new PackageDetailCallBack() {
        @Override
        public void success(String msg, RedPackageDetail detail) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
            if (null != detail) {
                singleAdapter = new SingleRedPackageAdapter(getSelfActivity(), detail);
                recordRecView.setAdapter(singleAdapter);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };
}
