package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.CreateGroupCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/5.
 */

public class CreateGroup extends BaseActivity implements View.OnClickListener, ChooseGroupContactAdapter.OnClickGroupInListener, ChooseGroupContactAdapter.OnChooseMemberListener {
    private static final int MAXCHOOSE = 4;
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> chooseMembers;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_group_create);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_hint_member = (TextView) findViewById(R.id.hint_member);

        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new ChooseGroupContactAdapter(this, friends, MAXCHOOSE);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
//        ArrayList<FriendInfo> friendInfos = getIntent().getParcelableArrayListExtra("friends");
//        friends = friendInfos;
//        adapter.modifyData(friends);
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(this,"加载中").show();
        FriendManagerRequest.queryFriendList(this,token,myFriendListCallback);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        btn_create.setOnClickListener(this);
        adapter.setOnChooseMemberListener(this);
        adapter.setOnClickGroupInListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.btn_create:
                createGroup();
                break;
        }
    }

    private void createGroup(){
        if(null != chooseMembers && chooseMembers.size() > 0) {
            String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");
            DialogUtils.showProgressDialog(this, "创建中");

            ArrayList<String> arrayList = new ArrayList<String>();
            for(FriendInfo info : chooseMembers) {
                arrayList.add(info.getMemberId());
            }
            String[] memberIdArray = arrayList.toArray(new String[arrayList.size()]);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("groupMembers", memberIdArray);

            GroupManagerRequest.createGroupReq(this, token, params, createGroupCallback);
        }else{
            ToastUtil.doToast(this,"请选择群成员");
        }
    }

    private void createSuccess(GroupInfo info) {
        Intent intent = new Intent();
        intent.putExtra("created", info);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void clickGroup() {
        ToastUtil.doToast(this, "选择已有群组");
    }

    @Override
    public void choose(ArrayList<FriendInfo> infos) {
        chooseMembers = infos;
        if (null != infos) {
            tv_hint_member.setText(String.format(getString(R.string.hint_choose_members), infos.size()));
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), MAXCHOOSE));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }

    CreateGroupCallback createGroupCallback = new CreateGroupCallback() {
        @Override
        public void success(String msg, GroupInfo groupInfo) {
            DialogUtils.closeProgressDialog();
            if(null != groupInfo) {
                //假数据
                groupInfo.setGroupName("途牛旅游01");
                groupInfo.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
                groupInfo.setUserCount("5");

                createSuccess(groupInfo);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(CreateGroup.this, msg + " -- 假数据");
            GroupInfo groupInfo = new GroupInfo();
            //假数据
            groupInfo.setGroupName("途牛旅游01");
            groupInfo.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
            groupInfo.setUserCount("5");
            createSuccess(groupInfo);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();

            GroupInfo groupInfo = new GroupInfo();
            //假数据
            groupInfo.setGroupName("途牛旅游01");
            groupInfo.setGroupUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
            groupInfo.setUserCount("5");
            createSuccess(groupInfo);
        }
    };

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            DialogUtils.closeProgressDialog();
            if(null !=  friendInfos) {
                for(FriendInfo info : friendInfos) {
                    if(null == info.getMemberName()) {
                        info.setMemberName("Null");
                    }
                }
                friends = friendInfos;
                adapter.modify(friendInfos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(CreateGroup.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };
}
