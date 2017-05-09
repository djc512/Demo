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
//                    GroupInfo info = new GroupInfo();
//                    //假数据
//                    info.setGroupName("途牛旅游01");
//                    info.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//                    info.setUserCount("5");

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
        }
    }

    GroupListCallback groupListCallback = new GroupListCallback() {
        @Override
        public void success(String msg, ArrayList<GroupInfo> groupInfos) {
            DialogUtils.closeProgressDialog();
            if(null != groups) {
//                adatper.modifyData(groups);
                //假数据
                groups.addAll(data());
                adatper.modifyData(groups);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupActivity.this, msg + " -- 假数据");
            //假数据
            groups.addAll(data());
            adatper.modifyData(groups);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            //假数据
            groups.addAll(data());
            adatper.modifyData(groups);
        }
    };

    private ArrayList<GroupInfo> data() {
        ArrayList<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
        GroupInfo group01 = new GroupInfo();
        group01.setGroupName("印家群");
        group01.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
        group01.setUserCount("3");

        GroupInfo group02 = new GroupInfo();
        group02.setGroupName("途牛旅游");
        group02.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");
        group02.setUserCount("4");

        groupInfos.add(group01);
        groupInfos.add(group02);
        return groupInfos;
    }
}
