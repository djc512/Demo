package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.CreateGroupCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/5.
 */

public class CreateGroup extends BaseActivity implements View.OnClickListener, ChooseGroupContactAdapter.OnClickGroupInListener, ChooseGroupContactAdapter.OnChooseMemberListener {
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> chooseMembers;
    private String token;
    private boolean isGoChat = false;
    private LoadingDialog loadingDialog;
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
        loadingDialog = new LoadingDialog(this);
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_hint_member = (TextView) findViewById(R.id.hint_member);

        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST, 1, ContextCompat.getColor(this, R.color.recycler_divider_color)));

        adapter = new ChooseGroupContactAdapter(this, friends);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        isGoChat = getIntent().getBooleanExtra("goChat", false);
        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
//        DialogUtils.showProgressDialog(this,"加载中").show();
        loadingDialog.show();
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
//            DialogUtils.showProgressDialog(this, "创建中").show();
            loadingDialog.show();

            ArrayList<String> arrayList = new ArrayList<String>();
            for(FriendInfo info : chooseMembers) {
                arrayList.add(info.getMemberId());
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("groupMembers", arrayList);

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
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), friends.size()));
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
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if(null != groupInfo) {
//                if (isGoChat) {
                    Intent intent = new Intent(CreateGroup.this, ChatTestActivity.class);
                    intent.putExtra("GroupInfo", groupInfo);
                    startActivity(intent);
                    EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
                    finish();
//                } else {
//                    createSuccess(groupInfo);
//                }
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(CreateGroup.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if(null !=  friendInfos) {
                for(FriendInfo info : friendInfos) {
                    if(null == info.getMemberName()) {
                        info.setMemberName("Null");
                    }
                }
                friends = friendInfos;
                btn_create.setText(String.format(getString(R.string.btn_hint_members), 0, friends.size()));

                adapter.modify(friends);
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(CreateGroup.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };
}
