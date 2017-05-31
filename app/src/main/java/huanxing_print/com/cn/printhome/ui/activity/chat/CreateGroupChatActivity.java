package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/5.
 */

public class CreateGroupChatActivity extends BaseActivity implements View.OnClickListener, ChooseGroupContactAdapter.OnClickGroupInListener, ChooseGroupContactAdapter.OnChooseMemberListener {
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> chooseMembers;
    private String token;
    private boolean isGoChat = false;
    private String imgUrl;
    private String fileUrl;
    private String toChatUsername;
    private String fileName;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        findViewById(R.id.click_bottom).setVisibility(View.GONE);
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_hint_member = (TextView) findViewById(R.id.hint_member);

        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new ChooseGroupContactAdapter(this, friends);
        recyclerView.setAdapter(adapter);

        imgUrl = getIntent().getStringExtra("imgUrl");
        fileUrl = getIntent().getStringExtra("fileUrl");
        fileName = getIntent().getStringExtra("fileName");
    }

    private void initData() {
        isGoChat = getIntent().getBooleanExtra("goChat", false);
        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(this, "加载中").show();
        FriendManagerRequest.queryFriendList(this, token, myFriendListCallback);
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

    private void createGroup() {
        if (null != chooseMembers && chooseMembers.size() > 0) {
            DialogUtils.showProgressDialog(this, "创建中").show();
            if (!ObjectUtils.isNull(fileUrl)) {
                //发送图片
                toChatUsername = chooseMembers.get(0).getEasemobId();
                sendFileMessage(fileUrl);
            } else if (!ObjectUtils.isNull(imgUrl)) {
                //发送图片
                toChatUsername = chooseMembers.get(0).getEasemobId();
                sendImageMessage(imgUrl);
            }
        }
    }


    @Override
    public void clickGroup() {
        ToastUtil.doToast(this, "选择已有群组");
    }

    @Override
    public void choose(ArrayList<FriendInfo> infos) {
        chooseMembers = infos;
        if (null != infos) {
//            tv_hint_member.setText(String.format(getString(R.string.hint_choose_members), infos.size()));
//            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), 1));
//            if (infos.size() > 0) {
//                btn_create.setEnabled(true);
//            } else {
//                btn_create.setEnabled(false);
//            }
            createGroup();
        }
    }


    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            DialogUtils.closeProgressDialog();
            if (null != friendInfos) {
                for (FriendInfo info : friendInfos) {
                    if (null == info.getMemberName()) {
                        info.setMemberName("Null");
                    }
                }
                friends = friendInfos;
                btn_create.setText(String.format(getString(R.string.btn_hint_members), 0, 1));

                adapter.modify(friends);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(CreateGroupChatActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };


    protected void sendImageMessage(String imagePath) {
        Log.d("CMCC", "imagePath:" + imagePath + ",toChatUsername:" + toChatUsername);
        EMMessage emMessage = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        emMessage.setAttribute("otherName", chooseMembers.get(0).getMemberName());
        emMessage.setAttribute("otherUrl", chooseMembers.get(0).getMemberUrl());
        sendMessage(emMessage);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage emMessage = EMMessage.createFileSendMessage(filePath, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        emMessage.setAttribute("otherName", chooseMembers.get(0).getMemberName());
        emMessage.setAttribute("otherUrl", chooseMembers.get(0).getMemberUrl());
        EMFileMessageBody body = (EMFileMessageBody) emMessage.getBody();
        body.setFileName(fileName);
        sendMessage(emMessage);
    }


    protected void sendMessage(EMMessage message) {
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //直接跳到聊天界面
        finish();
    }
}
