package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseSmileUtils;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.yinxin.RedPackageRecordActivity;

/**
 * 红包提醒布局
 */
public class EaseChatRowPackageHint extends EaseChatRowText {

    private TextView txt_message;
    private TextView timestamp;

    public EaseChatRowPackageHint(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_hint, this);
    }

    @Override
    protected void onFindViewById() {
        txt_message = (TextView) findViewById(R.id.txt_message);
        timestamp = (TextView) findViewById(R.id.timestamp);
    }


    @Override
    public void onSetUpView() {
        timestamp.setVisibility(GONE);
        // 设置内容
        //txt_message.setText(message.getStringAttribute("message", ""));
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        txt_message.setText(span, TextView.BufferType.SPANNABLE);
        handleTextMessage();
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, RedPackageRecordActivity.class);
        intent.putExtra("groupId", message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_ID,
                ""));
        intent.putExtra("type", 2);
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            intent.putExtra("singleType", false);
        } else {
            intent.putExtra("singleType", true);
        }
        intent.putExtra("packetId", message.getStringAttribute(EaseConstant.MESSAGE_HINT_PACKET_ID,
                ""));
        context.startActivity(intent);
    }
}
