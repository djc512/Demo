package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.event.contacts.GroupUpdate;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
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
 * Created by wanghao on 2017/5/10.
 */

public class GroupMemberAddActivity extends BaseActivity implements View.OnClickListener, ChooseGroupContactAdapter.OnClickGroupInListener, ChooseGroupContactAdapter.OnChooseMemberListener{
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> chooseMembers;
    private String currentGroupId;
    private ArrayList<GroupMember> groupMembers;
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

        adapter = new ChooseGroupContactAdapter(this, friends);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        currentGroupId = getIntent().getStringExtra("groupId");
        groupMembers = getIntent().getParcelableArrayListExtra("groupMember");
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
                addMemberToGroup();
                break;
        }
    }

    private void addMemberToGroup() {
        if(null != chooseMembers && chooseMembers.size() > 0) {
            String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");
            DialogUtils.showProgressDialog(this, "添加中").show();

            ArrayList<String> arrayList = new ArrayList<String>();
            for (FriendInfo info : chooseMembers) {
                arrayList.add(info.getMemberId());
            }

            Map<String, Object> params = new HashMap<String, Object>();
            Log.e("wanghao",currentGroupId);
            params.put("groupId", currentGroupId);
            params.put("memberIds", arrayList);
            GroupManagerRequest.addMemberToGroup(this, token, params, addMemberCallback);
        }else{
            ToastUtil.doToast(this,"请选择群成员");
        }
    }

    private void addMemberSuccess() {
        setResult(RESULT_OK);
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
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(),friends.size()));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }

    NullCallback addMemberCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            addMemberSuccess();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupMemberAddActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
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
                btn_create.setText(String.format(getString(R.string.btn_hint_members), 0, friends.size()));
                ArrayList<FriendInfo> chooseFriends = new ArrayList<FriendInfo>();
                if (groupMembers != null && groupMembers.size() > 0) {
                    for (FriendInfo friendInfo : friendInfos) {
                        if(!isGroupMember(friendInfo)) {
                            chooseFriends.add(friendInfo);
                        }
                    }
                }else {
                    chooseFriends = friendInfos;
                }
                friends = chooseFriends;
                adapter.modify(friends);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupMemberAddActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };

    private boolean isGroupMember(FriendInfo friendInfo) {
        for(GroupMember groupMember : groupMembers) {
            if(friendInfo.getMemberId().equals(groupMember.getMemberId())) {
                return true;
            }
        }
        return false;
    }
}
