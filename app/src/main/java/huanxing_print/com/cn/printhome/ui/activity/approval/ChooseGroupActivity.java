package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.approval.ChooseGroupEvent;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.contact.CreateGroup;
import huanxing_print.com.cn.printhome.ui.adapter.GroupAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * 选择群组界面
 * Created by wanghao on 2017/5/5.
 */

public class ChooseGroupActivity extends BaseActivity implements View.OnClickListener, GroupAdatper.OnItemGroupClickListener {
    private static final int CREATE_GROUP = 1000;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
    private GroupAdatper adatper;
    private TextView create_group;
    private String type;//区分两种审批

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        create_group = (TextView) findViewById(R.id.create_group);
        create_group.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.groupView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adatper = new GroupAdatper(this, groups);
        adatper.setOnItemGroupClickListener(this);
        recyclerView.setAdapter(adatper);
    }

    private void initData() {
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(this, "加载中").show();
        GroupManagerRequest.queryGroupList(this, token, groupListCallback);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.create_group).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.create_group:
                Intent intent = new Intent(this, CreateGroup.class);
                startActivityForResult(intent, CREATE_GROUP);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_GROUP:
                if (resultCode == RESULT_OK) {
                    GroupInfo info = data.getParcelableExtra("created");

                    groups.add(info);
                    adatper.modifyData(groups);
                }
                break;
        }
    }

    @Override
    public void clickGroup(GroupInfo info) {
        if (null != info) {
            //ToastUtil.doToast(this, info.getGroupName());
            //跳到选择群成员界面
            Intent intent = new Intent();
            intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
            intent.putExtra("groupId", info.getGroupId());
            intent.putExtra("type", type);
            startActivity(intent);
            EventBus.getDefault().post(new ChooseGroupEvent(info.getGroupId()));
            finish();
        }
    }

    GroupListCallback groupListCallback = new GroupListCallback() {
        @Override
        public void success(String msg, ArrayList<GroupInfo> groupInfos) {
            DialogUtils.closeProgressDialog();
            if (null != groupInfos) {
                groups = groupInfos;
                adatper.modifyData(groups);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ChooseGroupActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };

}
