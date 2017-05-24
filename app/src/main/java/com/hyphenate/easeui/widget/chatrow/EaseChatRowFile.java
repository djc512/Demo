package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.ui.EaseShowNormalFileActivity;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.TextFormater;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.ui.activity.chat.CreateGroupChatActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.PreViewUtil;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import huanxing_print.com.cn.printhome.view.popupwindow.PopupList;

import static huanxing_print.com.cn.printhome.util.webserver.ChatFileType.getExtensionName;

public class EaseChatRowFile extends EaseChatRow {

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;
    private ImageView iv_userhead;
    private ImageView iv_file_type;
    private TextView tv_userid;
    private ArrayList<String> popupMenuItemList;
    private String localFilePath;
    protected EMCallBack sendfileCallBack;
    private LinearLayout bubble;
    protected boolean isNotifyProcessed;
    private EMNormalFileMessageBody fileMessageBody;

    public EaseChatRowFile(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_file : R.layout.ease_row_sent_file, this);
    }

    @Override
    protected void onFindViewById() {
        fileNameView = (TextView) findViewById(R.id.tv_file_name);
        fileSizeView = (TextView) findViewById(R.id.tv_file_size);
        fileStateView = (TextView) findViewById(R.id.tv_file_state);
        percentageView = (TextView) findViewById(R.id.percentage);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        iv_file_type = (ImageView) findViewById(R.id.iv_file_type);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        bubble = (LinearLayout) findViewById(R.id.bubble);
    }


    @Override
    protected void onSetUpView() {
        fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
        localFilePath = filePath;
        fileNameView.setText(fileMessageBody.getFileName());
        if (message.getType() == EMMessage.Type.FILE) {
            //设置不同文件类型的图标
            String fileType = getExtensionName(filePath);
            if (!ObjectUtils.isNull(fileType)) {
                switch (fileType) {
                    case "pdf":
                        iv_file_type.setImageResource(R.drawable.file_pdf);
                        break;
                    case "doc":
                        iv_file_type.setImageResource(R.drawable.file_doc);
                        break;
                    case "docx":
                        iv_file_type.setImageResource(R.drawable.file_docx);
                        break;
                    case "ppt":
                        iv_file_type.setImageResource(R.drawable.file_ppt);
                        break;
                    case "pptx":
                        iv_file_type.setImageResource(R.drawable.file_pptx);
                        break;
                    default:
                        iv_file_type.setImageResource(R.drawable.file_doc);
                        break;
                }
            }
        }
        fileSizeView.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            File file = new File(filePath);
            if (file.exists()) {
                fileStateView.setText(R.string.Have_downloaded);
            } else {
                fileStateView.setText(R.string.Did_not_download);
            }
            return;
        }
        String iconUrl = message.getStringAttribute("iconUrl", "");
        String nickName = message.getStringAttribute("nickName", "");
//        Log.i("CCCC", "iconUrl=============================================" + iconUrl);
//        Log.i("PPPP", "iconUrl=============================================" + iconUrl);
        //头像
        if (ObjectUtils.isNull(iconUrl)) {
//            Log.i("PPPP", "=============================================我走了为空的");
//            Log.i("CCCC", "=============================================我走了为空的");
            Glide.with(getContext())
                    .load(R.drawable.iv_head)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        } else {
//            Log.i("PPPP", "=============================================我走了不为空，网络的");
//            Log.i("CCCC", "=============================================我走了不为空，网络的");
            Glide.with(getContext())
                    .load(iconUrl)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        }
        //昵称
        if (ObjectUtils.isNull(nickName)) {
            tv_userid.setText(message.getFrom());
//            Log.i("DDDD", "=============================================我走了空");
        } else {
            tv_userid.setText(nickName);
//            Log.i("DDDD", "=============================================我走了不为空，网络的");
        }
//        Log.i("DDDD", "message.getFrom()=============================================" + message.getFrom());
//        Log.i("DDDD", "nickName=============================================" + nickName);
        //隐藏发送文件的用户名
        if (message.getChatType().equals(ChatType.Chat)) {
            tv_userid.setVisibility(GONE);
        } else {
            tv_userid.setVisibility(VISIBLE);
        }

        // until here, to sending message
        handleSendMessage();
    }

    /**
     * handle sending message
     */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status()) {
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress() + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (file.exists()) {
            //文件预览页
            // open files if it exist
//            FileUtils.openFile(file, (Activity) context);
//            Intent intent = FileUtil.openFile(file.getAbsolutePath());
//            context.startActivity(intent);
            PreViewUtil.preview(context, filePath);
        } else {
            // download the file
            context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
        }
        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onBubbleLongClick() {
//        Log.d("CMCC", "onBubbleLongClick触发了");
        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (!file.exists()) {
            ToastUtils.showToast(context, "请下载之后再操作!");
            return;
        }
        popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("打印");
        popupMenuItemList.add("转发");
        popupMenuItemList.add("删除");
        PopupList popupList = new PopupList(context);
        popupList.bind(bubble, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                switch (position) {
                    case 0:
                        //打印
//                        Log.d("CMCC", "打印");
                        PreViewUtil.preview(context, localFilePath);
                        break;
                    case 1:
                        //转发
//                        Log.d("CMCC", "转发");
//                        Log.d("CMCC", "localFilePath:" + localFilePath);
                        //跳转到选择联系人界面只能单选
                        Intent intent = new Intent(context, CreateGroupChatActivity.class);
                        intent.putExtra("fileUrl", localFilePath);
                        context.startActivity(intent);
                        break;
                    case 2:
                        if (message.direct() == EMMessage.Direct.SEND) {
                            //只能删除本人发出的消息
                            //删除记录
//                            Log.d("CMCC", "删除");
                            //发送透传消息代码如下：
                            String action = context.getString(R.string.REMOVE);
                            EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);

                            EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
                            Log.d("CMCC", "111111");
                            if (message.getChatType() == ChatType.GroupChat ||
                                    message.getChatType() == ChatType.ChatRoom) {
                                cmdMessage.setChatType(ChatType.GroupChat);
                                String toChatUserName = message.getTo();
//                                Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                                cmdMessage.setTo(toChatUserName);
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            } else {
                                String toChatUserName = message.getFrom();
//                                Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                                cmdMessage.setFrom(toChatUserName);
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            }

                            String msgId = message.getMsgId();
//                            Log.d("CMCC", "msgIdsend-------->" + msgId);
                            cmdMessage.setAttribute("msgid", msgId);
                            cmdMessage.addBody(cmdBody);
                            EMClient.getInstance().chatManager().sendMessage(cmdMessage);
//                            Log.d("CMCC", "发送透传success");
                        } else {
                            //删除本地消息
                            if (message.getChatType() == ChatType.GroupChat ||
                                    message.getChatType() == ChatType.ChatRoom) {
                                String toChatUserName = message.getTo();
//                                Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            } else {
                                String toChatUserName = message.getFrom();
//                                Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            }
                        }
                        //发消息刷新
                        RefreshEvent event = new RefreshEvent();
                        event.setCode(0x13);
                        EventBus.getDefault().post(event);
                        break;
                }
            }
        });
    }
}
