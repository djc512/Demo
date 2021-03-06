package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseConversationList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatApprovalActivity;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.CreateGroup;
import huanxing_print.com.cn.printhome.util.Constant;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

public class ChatFragment extends BaseFragment implements OnClickListener {

    private final static int MSG_REFRESH = 2;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected LinearLayout errorItemContainer;

    protected boolean isConflict;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };

    @Override
    protected void init() {

        EventBus.getDefault().register(this);
        Logger.i("init");
        conversationListView = (EaseConversationList) findViewById(R.id.list);
        errorItemContainer = (LinearLayout) findViewById(R.id.fl_error_item);
        findViewById(R.id.addImg).setOnClickListener(this);

        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        //if (listItemClickListener != null) {
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                //单聊
                EMMessage otherLastMsg = conversation.getLatestMessageFromOthers();
                EMMessage lastMsg = conversation.getLastMessage();
                if ("notice".equals(conversation.getLastMessage().getUserName())) {
                    Log.e("CMCC", "notice");
                    Intent intent = new Intent(getActivity(), ChatApprovalActivity.class);
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    if (otherLastMsg != null) {
                        intent.putExtra(Constant.EXTRA_USER_ID, otherLastMsg.getFrom());
                        intent.putExtra("name", otherLastMsg.getStringAttribute("nickName", ""));
                    }
                    startActivity(intent);
                    return;
                }
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Intent intent = new Intent(getActivity(), ChatTestActivity.class);
                if (EMConversation.EMConversationType.GroupChat ==
                        conversation.getType()) {
//                    intent.putExtra("type", EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra(Constant.EXTRA_USER_ID, lastMsg.getTo());
                    if (ObjectUtils.isNull(otherLastMsg)) {
                        //别人没有说话
                        intent.putExtra("nickName", lastMsg.getStringAttribute("otherName", ""));
                        intent.putExtra("iconUrl", lastMsg.getStringAttribute("otherUrl", ""));
                    } else {
                        intent.putExtra("nickName", otherLastMsg.getStringAttribute("groupName", ""));
                        intent.putExtra("iconUrl", otherLastMsg.getStringAttribute("groupUrl", ""));
                    }
                } else if (EMConversation.EMConversationType.ChatRoom ==
                        conversation.getType()) {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
                    intent.putExtra(Constant.EXTRA_USER_ID, lastMsg.getTo());
                    if (ObjectUtils.isNull(otherLastMsg)) {
                        //别人没有说话
                        intent.putExtra("nickName", lastMsg.getStringAttribute("otherName", ""));
                        intent.putExtra("iconUrl", lastMsg.getStringAttribute("otherUrl", ""));
                    } else {
                        intent.putExtra("nickName", otherLastMsg.getStringAttribute("groupName", ""));
                        intent.putExtra("iconUrl", otherLastMsg.getStringAttribute("groupUrl", ""));
                    }
                } else {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    if (ObjectUtils.isNull(otherLastMsg)) {
                        //别人没有说话
                        intent.putExtra(Constant.EXTRA_USER_ID, lastMsg.getTo());
                        intent.putExtra("nickName", lastMsg.getStringAttribute("otherName", ""));
                        intent.putExtra("iconUrl", lastMsg.getStringAttribute("otherUrl", ""));
                    } else {
                        intent.putExtra(Constant.EXTRA_USER_ID, otherLastMsg.getFrom());
                        intent.putExtra("nickName", otherLastMsg.getStringAttribute("nickName", ""));
                        intent.putExtra("iconUrl", otherLastMsg.getStringAttribute("iconUrl", ""));
                    }
                }
                startActivity(intent);
                //listItemClickListener.onListItemClicked(conversation);
            }
        });
        //}

        EMClient.getInstance().addConnectionListener(connectionListener);
    }


    @Override
    protected int getContextView() {
        return R.layout.frag_chat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImg:
////                跳转到联系人界面
//                startActivity(new Intent(getActivity(), ListAddContactActivity.class));
                Intent goCreateGroup = new Intent(getActivity(), CreateGroup.class);
                goCreateGroup.putExtra("goChat", true);
                startActivity(goCreateGroup);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
        refresh();
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * refresh ui
     */
    public void refresh() {
        handler.sendEmptyMessage(MSG_REFRESH);
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (!hidden) {
        refresh();
        //}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(RefreshEvent event) {
        if (0x14 == event.getCode()) {
            Log.d("CMCC", "接收到消息传过来了!!");
            refresh();
        }
    }


}
