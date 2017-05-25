package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.event.contacts.GroupMessageUpdate;
import huanxing_print.com.cn.printhome.event.contacts.GroupUpdate;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.GroupMembersAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/10.
 */

public class GroupSettingActivity extends BaseActivity implements View.OnClickListener, GroupMembersAdapter
        .OnGroupMemberClickListener {
    private static final int ADD_MEMBER = 10000;
    private GroupMembersAdapter adapter;
    private ScrollGridView memberGridView;
    private GroupMessageInfo groupMessageInfo;
    private LinearLayout ll_transfer;
    private LinearLayout ll_back;
    private TextView tv_groupName;
    private TextView tv_balance;
    private TextView title_group_name;
    private ArrayList<GroupMember> groupMembers = new ArrayList<GroupMember>();
    private String currentGroupId;
    private String easemobGroupId;
    private String token;
    private static final int transferRequsetCoder = 1;//修改群主的请求码
    private GroupMember delGroupMember;
    private static final int modifynameRequsetCoder = 2;//修改群昵称的请求码

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_group_setting);
        initView();
        initData();
        setListener();
        setData();
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        ll_transfer.setOnClickListener(this);
        findViewById(R.id.ll_dissolution).setOnClickListener(this);
        findViewById(R.id.ll_contactfile).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);
        findViewById(R.id.ll_modifyname).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
    }

    private void initView() {
        title_group_name = (TextView) findViewById(R.id.title_group_name);
        ll_transfer = (LinearLayout) findViewById(R.id.ll_transfer);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        memberGridView = (ScrollGridView) findViewById(R.id.gv_group_members);
        adapter = new GroupMembersAdapter(this, groupMembers, false);
        adapter.setOnGroupMemberClickListener(this);
        memberGridView.setAdapter(adapter);

        tv_groupName = (TextView) findViewById(R.id.tv_group_name);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
    }

    private void initData() {
        currentGroupId = getIntent().getStringExtra("groupId");
        easemobGroupId = getIntent().getStringExtra("easemobGroupId");
        Logger.i(currentGroupId);
        Logger.i(easemobGroupId);
        Logger.i(BaseApplication.getInstance().getEasemobId());

        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        queryGroupMsg();
//        groupMessageInfo = new GroupMessageInfo();
//        ArrayList<GroupMember> groupMembers = new ArrayList<GroupMember>();
//        GroupMember member01 = new GroupMember();
//        member01.setType("1");
//        member01.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member02 = new GroupMember();
//        member02.setType("0");
//        member02.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//        GroupMember member03 = new GroupMember();
//        member03.setType("0");
//        member03.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member04 = new GroupMember();
//        member04.setType("0");
//        member04.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member05 = new GroupMember();
//        member05.setType("0");
//        member05.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member06 = new GroupMember();
//        member06.setType("0");
//        member06.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member07 = new GroupMember();
//        member07.setType("0");
//        member07.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member08 = new GroupMember();
//        member08.setType("0");
//        member08.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member09 = new GroupMember();
//        member09.setType("0");
//        member09.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member10 = new GroupMember();
//        member10.setType("0");
//        member10.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member11 = new GroupMember();
//        member11.setType("0");
//        member11.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member12 = new GroupMember();
//        member12.setType("0");
//        member12.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member13 = new GroupMember();
//        member13.setType("0");
//        member13.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member14 = new GroupMember();
//        member14.setType("0");
//        member14.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        GroupMember member15 = new GroupMember();
//        member15.setType("0");
//        member15.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1
// &src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115
// %2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
//        GroupMember member16 = new GroupMember();
//        member16.setType("0");
//        member16.setMemberUrl("https://timgsa.baidu
// .com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src
// =http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
//
//        groupMembers.add(member01);
//        groupMembers.add(member02);
//        groupMembers.add(member03);
//        groupMembers.add(member04);
//        groupMembers.add(member05);
//        groupMembers.add(member06);
//        groupMembers.add(member07);
//        groupMembers.add(member08);
//        groupMembers.add(member09);
//        groupMembers.add(member10);
//        groupMembers.add(member11);
//        groupMembers.add(member12);
//        groupMembers.add(member13);
//        groupMembers.add(member14);
//        groupMembers.add(member15);
//        groupMembers.add(member16);
//
//        groupMessageInfo.setGroupMembers(groupMembers);
//        groupMessageInfo.setGroupName("哈哈");
//        groupMessageInfo.setBalance("200.0");
//        groupMessageInfo.setIsManage("1");
//
//        adapter.modify(groupMessageInfo.getGroupMembers(),"1".equals(groupMessageInfo.getIsManage()) ? true : false);
    }

    public void setData() {
        if (groupMessageInfo != null) {
            currentGroupId = groupMessageInfo.getGroupId();
            title_group_name.setText(String.format("群管理(%s)", String.valueOf(groupMessageInfo.getGroupMembers().size
                    ())));
            tv_groupName.setText(groupMessageInfo.getGroupName());
            tv_balance.setText(String.format("%s元", groupMessageInfo.getBalance() == null ? "0" : groupMessageInfo
                    .getBalance()));
            if ("1".equals(groupMessageInfo.getIsManage())) {
                findViewById(R.id.part_show_manager).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_modifyname).setEnabled(true);
            } else {
                findViewById(R.id.part_show_manager).setVisibility(View.GONE);
                findViewById(R.id.ll_modifyname).setEnabled(false);
            }

            adapter.modify(groupMessageInfo.getGroupMembers(), "1".equals(groupMessageInfo.getIsManage()) ? true :
                    false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.ll_transfer:
                Intent transferIntent = new Intent(getSelfActivity(), GroupOwnerTransferActivity.class);
                transferIntent.putExtra("qunlist", groupMessageInfo);
                startActivityForResult(transferIntent, transferRequsetCoder);
                break;
            case R.id.ll_dissolution:
                DialogUtils.showQunDissolutionDialog(getSelfActivity(), "确认解散此群吗？", new DialogUtils
                        .QunOwnerDissolutionDialogCallBack() {
                    @Override
                    public void dissolution() {
                        DialogUtils.showProgressDialog(getSelfActivity(), "努力解散").show();
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("groupId", currentGroupId);
                        GroupManagerRequest.dissolution(getSelfActivity(), baseApplication.getLoginToken(), params,
                                dissolutinQunCallBack);
                    }

                    @Override
                    public void cancle() {
                        DialogUtils.closeProgressDialog();
                    }
                }).show();
                break;
            case R.id.ll_contactfile:
                Bundle bundle = new Bundle();
                bundle.putString(ChatFilesActivity.GROUP_ID, easemobGroupId);
                ChatFilesActivity.start(GroupSettingActivity.this, bundle);
                break;
            case R.id.ll_clear:
                DialogUtils.showQunDissolutionDialog(getSelfActivity(), "确定清空群记录", new DialogUtils
                        .QunOwnerDissolutionDialogCallBack() {
                    @Override
                    public void dissolution() {
                        clearChatHistory(easemobGroupId);
                    }

                    @Override
                    public void cancle() {
                        DialogUtils.closeProgressDialog();
                    }
                }).show();
                break;
            case R.id.ll_modifyname:
                if (null != groupMessageInfo) {
                    Intent modifyIntent = new Intent(getSelfActivity(), ModifyQunNameActivity.class);
                    modifyIntent.putExtra("groupMessageInfo", groupMessageInfo);
                    modifyIntent.putExtra("groupid", currentGroupId);
                    modifyIntent.putExtra("groupurl", groupMessageInfo.getGroupUrl());
                    startActivityForResult(modifyIntent, modifynameRequsetCoder);
                } else {
                    ToastUtil.doToast(GroupSettingActivity.this, "还没有查到群信息");
                }
                break;
            case R.id.btn_exit:
                DialogUtils.showexitGroupDialog(getSelfActivity(), "您确定要退群吗?", new DialogUtils
                        .ExitGroupDialogCallback() {
                    @Override
                    public void cancle() {
                        DialogUtils.closeProgressDialog();
                    }

                    @Override
                    public void exit() {
                        exitGroupReq();
                    }
                }).show();
                break;
        }
    }


    private void exitGroupReq() {
        DialogUtils.showProgressDialog(this, "退群中").show();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", currentGroupId);
        GroupManagerRequest.exitGroup(this, token, params, exitGroupCallback);
    }

    /**
     * 清空群聊天记录
     *
     * @param groupId 群id
     */
    private void clearChatHistory(String groupId) {

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId, EMConversation
                .EMConversationType.GroupChat);
        if (conversation != null) {
            conversation.clearAllMessages();
        }
        Toast.makeText(this, "清空成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delMember(GroupMember member) {
        showHintMemberDel(member);
    }

    @Override
    public void clickMember(GroupMember member) {
    }

    @Override
    public void clickAdd() {
        Intent intent = new Intent(GroupSettingActivity.this, GroupMemberAddActivity.class);
        intent.putExtra("groupId", currentGroupId);
        intent.putExtra("groupMember", groupMessageInfo.getGroupMembers());
        startActivityForResult(intent, ADD_MEMBER);
    }

    private void showHintMemberDel(final GroupMember member) {
        String message = String.format("您要删除群成员 %s?", member.getMemberName());
        DialogUtils.showGroupMemDelDialog(this, message, new DialogUtils.GroupDelMemDialogCallback() {
            @Override
            public void del() {
                delGroupMember(member);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_MEMBER:
                if (resultCode == RESULT_OK)
                    queryGroupMsg();
                break;
            case transferRequsetCoder:
                if (resultCode == RESULT_OK)
                    queryGroupMsg();
                break;
            case modifynameRequsetCoder:
                if (resultCode == RESULT_OK)
                    queryGroupMsg();
                break;
        }
    }

    private void delGroupMember(GroupMember member) {
        delGroupMember = member;
        DialogUtils.showProgressDialog(this, "删除中").show();

        Map<String, Object> params = new HashMap<String, Object>();
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(member.getMemberId());
        params.put("groupId", currentGroupId);
        params.put("memberIds", arrayList);
        GroupManagerRequest.delMemberFromGroup(this, token, params, delMemberCallback);
    }

    private void delMemberSuccess() {
        if (null != delGroupMember) {
            groupMessageInfo.getGroupMembers().remove(delGroupMember);
            EventBus.getDefault().post(new GroupMessageUpdate("updateName", groupMessageInfo));
            adapter.modify(groupMessageInfo.getGroupMembers(), "1".equals(groupMessageInfo.getIsManage()) ? true :
                    false);
            title_group_name.setText(String.format("群管理(%s)", String.valueOf(groupMessageInfo.getGroupMembers().size
                    ())));
        }
    }

    private void queryGroupMsg() {
        DialogUtils.showProgressDialog(this, "加载中").show();
        GroupManagerRequest.queryGroupMessage(this, token, currentGroupId, easemobGroupId, groupMessageCallback);
    }

    GroupMessageCallback groupMessageCallback = new GroupMessageCallback() {
        @Override
        public void success(String msg, GroupMessageInfo groupMessageInfo) {
            DialogUtils.closeProgressDialog();
            GroupSettingActivity.this.groupMessageInfo = groupMessageInfo;
            setData();
            EventBus.getDefault().post(new GroupMessageUpdate("updateName", groupMessageInfo));
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
        }
    };

    NullCallback delMemberCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            delMemberSuccess();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupSettingActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };

    NullCallback exitGroupCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupSettingActivity.this, "退群成功");
            EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            finish();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(GroupSettingActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };

    NullCallback dissolutinQunCallBack = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            Toast.makeText(getSelfActivity(), "解散成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    };

    @Subscribe
    public void onUpdateGroupMessage(GroupMessageUpdate messageUpdate) {
        if ("updateName".equals(messageUpdate.getTag())) {
            tv_groupName.setText(messageUpdate.getGroupMessageInfo().getGroupName());
        }
    }
}
