package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.GroupAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupActivity extends BaseActivity implements View.OnClickListener,GroupAdatper.OnItemGroupClickListener {
    private static final int CREATE_GROUP = 1000;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
    private GroupAdatper adatper;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_group);
        initView();
        initData();
        setListener();
    }

    private void initView() {
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
        DialogUtils.showProgressDialog(this,"加载中");
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
                ToastUtil.doToast(this, "发起群组");
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
                if(resultCode == RESULT_OK) {
                    GroupInfo info = data.getParcelableExtra("created");

                    groups.add(info);
                    adatper.modifyData(groups);
                }
                break;
        }
    }

    @Override
    public void clickGroup(GroupInfo info) {
        if(null != info) {
            ToastUtil.doToast(this, info.getGroupName());
            //假设跳入到群设置详情，后续删除
            Intent intent = new Intent(GroupActivity.this, GroupSettingActivity.class);
            intent.putExtra("groupId", info.getGroupId());
            startActivity(intent);

        }
    }

    GroupListCallback groupListCallback = new GroupListCallback() {
        @Override
        public void success(String msg, ArrayList<GroupInfo> groupInfos) {
            DialogUtils.closeProgressDialog();
            if(null != groupInfos) {
                groups = groupInfos;
                adatper.modifyData(groups);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };

}
