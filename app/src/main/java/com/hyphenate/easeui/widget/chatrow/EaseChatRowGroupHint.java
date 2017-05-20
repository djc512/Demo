package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupManagerApprovalCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * 群提醒布局
 */
public class EaseChatRowGroupHint extends EaseChatRowText {

    private TextView txt_message;
    private TextView timestamp;
    private TextView txt_approval;
    private boolean CANCLICK = true;

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
        txt_approval = (TextView) findViewById(R.id.txt_approval);
    }


    @Override
    public void onSetUpView() {
        timestamp.setVisibility(GONE);
        // 设置内容
        String msg = message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_MESSAGE, "");
        String type = message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_MESSAGE_TYPE, "");
        //501特殊处理
        if ("501".equals(type)) {
            txt_approval.setVisibility(VISIBLE);
        } else {
            txt_approval.setVisibility(GONE);
        }
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
        String type = message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_MESSAGE_TYPE, "");
        String groupId = message.getStringAttribute(EaseConstant.MESSAGE_HINT_GROUP_ID_MANAGER, "");
        String memberId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_GROUP_HINT_APPLY_ID, "");
        Log.d("CMCC", "groupId:" + groupId + ",memberId:" + memberId + ",type:" + type);
        if ("501".equals(type)) {
            if (CANCLICK) {
                //可点击
                DialogUtils.showProgressDialog(context, "审批中").show();
                //点击事件
                String token = SharedPreferencesUtils.getShareString(context, ConFig.SHAREDPREFERENCES_NAME,
                        "loginToken");
                GroupManagerRequest.groupManagerApproval(context, token, groupId, memberId, callback);
            }

        }
    }

    GroupManagerApprovalCallback callback = new GroupManagerApprovalCallback() {
        @Override
        public void success(String msg) {
            Log.d("CMCC", "GroupManagerApprovalCallback:" + msg);
            CANCLICK = false;
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void fail(String msg) {
            Log.d("CMCC", "GroupManagerApprovalCallback:" + msg);
            CANCLICK = true;
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            Log.d("CMCC", "GroupManagerApprovalCallback connectFail");
            CANCLICK = true;
            DialogUtils.closeProgressDialog();
        }
    };
}
