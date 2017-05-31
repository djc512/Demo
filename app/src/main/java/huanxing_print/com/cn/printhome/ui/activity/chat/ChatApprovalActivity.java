package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Constant;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

import static huanxing_print.com.cn.printhome.R.id.ll_back;

/**
 * Created by htj on 2017/5/18.
 */

public class ChatApprovalActivity extends BaseActivity implements View.OnClickListener {
    private Context ctx;
    private ApprovalListAdapter lvAdapter;
    private XRefreshView xrf_czrecord;
    private ListView lv_my_list;
    private ArrayList<ApprovalObject> datalist = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isLoadMore = false;
    private String token;
    protected EaseChatMessageList messageList;
    private ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected int chatType;
    protected String toChatUsername;
    protected EMConversation conversation;
    protected int pagesize = 20;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_approval_notify);
        CommonUtils.initSystemBar(this);
        ctx = this;
        chatType = getIntent().getIntExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        toChatUsername = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        //Log.i("CMCC", "chatType:" + chatType + ",toChatUsername:" + toChatUsername);
        initView();
        initData();
    }

    private void initView() {
        // message list layout
        findViewById(R.id.ll_back).setOnClickListener(this);
        messageList = (EaseChatMessageList) findViewById(R.id.message_list);
        listView = messageList.getListView();

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
    }

    protected EaseChatFragment.EaseChatFragmentHelper chatFragmentHelper;

    private void initData() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {

            }

            @Override
            public void onUserAvatarLongClick(String username) {
            }

            @Override
            public void onResendClick(final EMMessage message) {
            }

            @Override
            public boolean onBubbleLongClick(EMMessage message) {
                return false;
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                Log.i("CCCP", "chatFragmentHelper+++++++++++++++++++" + chatFragmentHelper);
                if (chatFragmentHelper == null) {
                    return false;
                }
                if (!ObjectUtils.isNull(message.getStringAttribute("packetId", ""))) {
                    ToastUtil.doToast(getSelfActivity(), "点击了红包!");
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ll_back:
                finishCurrentActivity();
                break;

        }
    }

}
