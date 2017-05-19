package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

/**
 * 群提醒布局
 */
public class EaseChatRowGroupHint extends EaseChatRowText {

    private TextView txt_message;
    private TextView timestamp;

    public EaseChatRowGroupHint(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_group_hint, this);
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
        String msg = message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_MESSAGE, "");
        if (!ObjectUtils.isNull(msg)) {
            txt_message.setText(msg);
            Log.d("CMCC", "msg:" + msg);
        }
        handleTextMessage();
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

    }
}
