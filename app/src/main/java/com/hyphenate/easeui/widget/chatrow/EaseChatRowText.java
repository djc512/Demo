package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.exceptions.HyphenateException;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    private ImageView iv_userhead;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private String type;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
//        Log.i("CCCC","type======================="+message.getStringAttribute("type",""));
//        Log.i("CCCC","approveId======================="+message.getStringAttribute("approveId",""));
//        Log.i("CCCC","getFrom======================="+message.getFrom());
//        Log.i("CCCC","getto======================="+message.getTo());
        type = message.getStringAttribute("type", "");

    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        bubble = (RelativeLayout) findViewById(R.id.bubble);
    }

    @Override
    public void onSetUpView() {

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        if ("302".equals(type)) {
            String content = txtBody.getMessage() + "，立即查看";
            int fStart = content.indexOf("立即查看");
            int fEnd = fStart + "立即查看".length();
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_yellow)), fStart, fEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            contentView.setText(style);
        } else {
            Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
            // 设置内容
            contentView.setText(span, BufferType.SPANNABLE);
        }

        String iconUrl = message.getStringAttribute("iconUrl", "");
        String nickName = message.getStringAttribute("nickName", "");

        //如果是印信就写死名称和头像========================================================================
        //Log.i("CCCC","印家的Username============================================="+message.getUserName());
        if (message.getUserName().equals("secretary")) {
            iv_userhead.setImageResource(R.drawable.king);
        } else {
            //头像
            if (ObjectUtils.isNull(iconUrl)) {
                Glide.with(getContext())
                        .load(R.drawable.iv_head)
                        .transform(new CircleTransform(getContext()))
                        .into(iv_userhead);
            } else {
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
        }

        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

    }
}
