package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
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
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.PreViewUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;
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
    private File file;
    private PopupList popupList;
    private float mRawX;
    private float mRawY;
    private String fileReName;//修改之后的文件名称

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

        bubble.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                return false;
            }
        });

        popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("打印");
        popupMenuItemList.add("转发");
        popupMenuItemList.add("保存");
        popupMenuItemList.add("删除");

        popupList = new PopupList(context);

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
                        iv_file_type.setImageResource(R.drawable.iv_apply_copy);
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
            if (!ObjectUtils.isNull(filePath)) {
                if (FileType.isPrintType(filePath)) {
                    PreViewUtil.preview(context, filePath, true);
                } else {
                    ToastUtil.doToast(context, "不支持此类文件的预览!");
                }
            }
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
        file = new File(localFilePath);

        popupList.showPopupListWindow(bubble, position, mRawX, mRawY, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                switch (position) {
                    case 0:
                        //打印
                        if (!ObjectUtils.isNull(file) && file.exists()) {
                            //文件预览页
                            PreViewUtil.preview(context, localFilePath, false);
                        } else {
                            // download the file
                            ToastUtils.showToast(context, "下载中...");
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
                        break;
                    case 1:
                        //转发
                        EMFileMessageBody body = (EMFileMessageBody) message.getBody();
                        if (!ObjectUtils.isNull(file) && file.exists()) {
                            //跳转到选择联系人界面只能单选
                            Intent intent = new Intent(context, CreateGroupChatActivity.class);
                            intent.putExtra("fileUrl", localFilePath);
                            intent.putExtra("fileName", body.getFileName());
                            context.startActivity(intent);
                        } else {
                            context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
                            if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
                                try {
                                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                                } catch (HyphenateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case 2:
                        //保存
                        if (file.exists()) {
                            //文件预览页
                            ToastUtils.showToast(context, "保存成功!");
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
                        break;
                    case 3:
                        //删除本地消息
                        if (message.getChatType() == ChatType.GroupChat ||
                                message.getChatType() == ChatType.ChatRoom) {
                            String toChatUserName = message.getTo();
                            //删除掉本地消息
                            EMClient.getInstance().chatManager()
                                    .getConversation(toChatUserName).removeMessage(message.getMsgId());
                        } else {
                            if (message.direct() == EMMessage.Direct.SEND) {
                                //自己发的
                                String toChatUserName = message.getTo();
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            } else if (message.direct() == EMMessage.Direct.RECEIVE) {
                                String toChatUserName = message.getFrom();
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
