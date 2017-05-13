package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ChooseMemberEvent;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupMemberAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * description: 选择审批人和抄送人
 * author LSW
 * date 2017/5/8 20:25
 * update 2017/5/8
 */
public class ChoosePeopleOfAddressActivity extends BaseActivity implements
        View.OnClickListener,
        ChooseGroupMemberAdapter.OnClickGroupInListener,
        ChooseGroupMemberAdapter.OnChooseMemberListener {

    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupMemberAdapter groupMemberAdapter;
    private String groupId;//群组id
    private ArrayList<GroupMember> groupMembers = new ArrayList<>();
    private ArrayList<GroupMember> chooseGroupMembers;//选择的群组成员
    private String type;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_choose_people_of_address);

        initView();
        initData();
        setListener();
    }

    private void initView() {
        groupId = getIntent().getStringExtra("groupId");
        type = getIntent().getStringExtra("type");
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_hint_member = (TextView) findViewById(R.id.hint_member);

        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        groupMemberAdapter = new ChooseGroupMemberAdapter(getSelfActivity(), groupMembers);
        recyclerView.setAdapter(groupMemberAdapter);
    }

    private void initData() {
//        ArrayList<FriendInfo> friendInfos = getIntent().getParcelableArrayListExtra("groupMembers");
//        groupMembers = friendInfos;
//        adapter.modify(groupMembers);

        Log.i("CMCC", "groupId:" + groupId);
        //请求群数据
        DialogUtils.showProgressDialog(this, "加载中").show();
        GroupManagerRequest.queryGroupMessage(getSelfActivity(), baseApplication.getLoginToken(),
                groupId, groupMessageCallback);
    }

    GroupMessageCallback groupMessageCallback = new GroupMessageCallback() {
        @Override
        public void success(String msg, GroupMessageInfo groupMessageInfo) {
            DialogUtils.closeProgressDialog();
            if (!ObjectUtils.isNull(groupMessageInfo)) {
                if (!ObjectUtils.isNull(groupMessageInfo.getGroupMembers())) {
                    groupMembers = groupMessageInfo.getGroupMembers();
                    btn_create.setText(String.format(getString(R.string.btn_hint_members), 0, groupMembers.size()));
                    groupMemberAdapter.modify(groupMembers);
                }
            }
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

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        btn_create.setOnClickListener(this);
        groupMemberAdapter.setOnChooseMemberListener(this);
        groupMemberAdapter.setOnClickGroupInListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.btn_create:
                //返回
                //区分两种
                if ("approvalFriends".equals(type)) {
                    //审批
                    EventBus.getDefault().post(new ChooseMemberEvent(0x11, chooseGroupMembers));
                } else if ("copyFriends".equals(type)) {
                    //抄送
                    EventBus.getDefault().post(new ChooseMemberEvent(0x12, chooseGroupMembers));
                }
                finish();
                break;
        }
    }

    @Override
    public void clickGroup() {
        ToastUtil.doToast(this, "选择已有群组");
    }

    @Override
    public void choose(ArrayList<GroupMember> infos) {
        chooseGroupMembers = infos;
        if (null != infos) {
            tv_hint_member.setText(String.format(getString(R.string.hint_choose_members), infos.size()));
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), groupMembers.size()));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }
}
