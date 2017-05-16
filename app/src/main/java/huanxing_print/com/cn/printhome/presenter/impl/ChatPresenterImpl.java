package huanxing_print.com.cn.printhome.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import huanxing_print.com.cn.printhome.listener.EmsCallBackListener;
import huanxing_print.com.cn.printhome.presenter.ChatPresenter;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatView;

/**
 * 作者： itheima
 * 时间：2016-10-18 17:19
 * 网址：http://www.itheima.com
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    private int type = 1;
    private static final int TEXT = 0;//文本
    private static final int PIC = 1;//图片

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
    }

    @Override
    public void initChat(String contact,int kind) {

        /**
         * 1. 如果曾经跟contact有聊天过，那么获取最多最近的20条聊天记录，然后展示到View层
         * 2. 如果没有聊天过，返回一个空的List
         */
        updateChatData(contact,kind);
        mChatView.onInit(mEMMessageList);
    }

    @Override
    public void updateData(String username,int kind) {
        updateChatData(username,kind);
        mChatView.onUpdate(mEMMessageList.size());
    }

    @Override
    public void sendMessage(String username, String msg, int chatType,int kind) {
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg, username);

        Log.i("CMCC", "chatType:" + chatType);
        //给发送的消息添加kind属性
        emMessage.setAttribute("kind",kind);

        //如果是群聊，设置chattype，默认是单聊
        if (chatType == type) {
            Log.i("CMCC", "22222222");
            emMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        //emMessage.setChatType(EMMessage.ChatType.GroupChat);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);
        mChatView.onUpdate(mEMMessageList.size());
        emMessage.setMessageStatusCallback(new EmsCallBackListener() {
            @Override
            public void onMainSuccess() {
                mChatView.onUpdate(mEMMessageList.size());
            }

            @Override
            public void onMainError(int i, String s) {
                mChatView.onUpdate(mEMMessageList.size());
            }
        });

        EMClient.getInstance().chatManager().sendMessage(emMessage);


    }

    @Override
    public void sendImgMessage(String toChatUsername, String url,int chatType,int kind) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage emMessage =EMMessage.createImageSendMessage(url, false, toChatUsername);
        //给发送的消息添加kind属性
        emMessage.setAttribute("kind",kind);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == type)
            emMessage.setChatType(EMMessage.ChatType.GroupChat);

        //emMessage.setChatType(EMMessage.ChatType.GroupChat);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);
        mChatView.onUpdate(mEMMessageList.size());
        emMessage.setMessageStatusCallback(new EmsCallBackListener() {
            @Override
            public void onMainSuccess() {
                mChatView.onUpdate(mEMMessageList.size());
            }

            @Override
            public void onMainError(int i, String s) {
                mChatView.onUpdate(mEMMessageList.size());
            }
        });

        EMClient.getInstance().chatManager().sendMessage(emMessage);

    }

    private void updateChatData(String contact,int kind) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contact);
        if (conversation != null) {
            //需要将所有的未读消息标记为已读
            conversation.markAllMessagesAsRead();

            //曾经有聊天过
            //那么获取最多最近的20条聊天记录，然后展示到View层
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最后一条消息之前的19条（最多）
            int count = 19;
            if (mEMMessageList.size() >= 19) {
                count = mEMMessageList.size();
            }
            List<EMMessage> messageList = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            Collections.reverse(messageList);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(messageList);
            Collections.reverse(mEMMessageList);
        } else {
            mEMMessageList.clear();
        }
    }
}
