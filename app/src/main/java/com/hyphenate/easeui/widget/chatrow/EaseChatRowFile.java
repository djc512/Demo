package com.hyphenate.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.ui.EaseShowNormalFileActivity;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.TextFormater;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.file.FileUtil;

import static huanxing_print.com.cn.printhome.util.webserver.ChatFileType.getExtensionName;

public class EaseChatRowFile extends EaseChatRow {

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;
    private ImageView iv_userhead;
    private ImageView iv_file_type;
    private TextView tv_userid;

    protected EMCallBack sendfileCallBack;

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
    }


    @Override
    protected void onSetUpView() {
        fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
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
        Log.i("CCCC", "iconUrl=============================================" + iconUrl);
        Log.i("PPPP","iconUrl============================================="+iconUrl);
        //头像
        if (ObjectUtils.isNull(iconUrl)) {
            Log.i("PPPP","=============================================我走了为空的");
            Log.i("CCCC", "=============================================我走了为空的");
            Glide.with(getContext())
                    .load(R.drawable.iv_head)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        } else {
            Log.i("PPPP","=============================================我走了不为空，网络的");
            Log.i("CCCC", "=============================================我走了不为空，网络的");
            Glide.with(getContext())
                    .load(iconUrl)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        }
        //昵称
        if (ObjectUtils.isNull(nickName)) {
            tv_userid.setText(message.getFrom());
        } else {
            tv_userid.setText(nickName);
        }
        //隐藏发送文件的用户名
        //tv_userid.setVisibility(GONE);
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
            // open files if it exist
            FileUtils.openFile(file, (Activity) context);
            Intent intent = FileUtil.openFile(file.getAbsolutePath());
            context.startActivity(intent);
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
}
