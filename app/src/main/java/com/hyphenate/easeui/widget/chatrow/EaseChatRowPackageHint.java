package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.yinxin.RedPackageRecordActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

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
        String msg = message.getStringAttribute(EaseConstant.MESSAGE_HINT_MESSAGE, "");
        if (!ObjectUtils.isNull(msg)) {
            SpannableStringBuilder style=new SpannableStringBuilder(msg);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow3)),msg.length() - 2,msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置内容
            txt_message.setText(style);
        }
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
