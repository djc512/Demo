package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.chat.CommonPackage;
import huanxing_print.com.cn.printhome.model.chat.GroupLuckyPackageDetail;
import huanxing_print.com.cn.printhome.model.chat.RedPackageDetail;
import huanxing_print.com.cn.printhome.net.callback.chat.GetCommonPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetLuckyPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.PackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.ReceivedPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.RobPackageCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalApplyDetailsActivity;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalBuyAddOrRemoveActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;
import huanxing_print.com.cn.printhome.ui.activity.yinxin.RedPackageRecordActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.FailureRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.GroupRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.FailureRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GoneRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GroupRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.SingleRedEnvelopesDialog;

public class EaseChatRowText extends EaseChatRow {

    private TextView approvalName;
    private TextView approvalTime;
    private TextView approvalNumber;
    private TextView contentView;
    private ImageView iv_userhead;
    private LinearLayout approval_notify;
    private RelativeLayout textAll;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private FailureRedEnvelopesDialog dialog;
    private SingleRedEnvelopesDialog singleDialog;
    private GroupRedEnvelopesDialog groupDialog;
    private GoneRedEnvelopesDialog goneDialog;
    private String lingQuRenNickName;//发红包人的昵称
    private String lingQuRenId;//发红包人的id
    private String type;
    private Intent intent;
    private String approveId;
    private TextView tv_from;
    private TextView tv_chatcontent;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        String packetId = message.getStringAttribute("packetId", "");
        String packetType = message.getStringAttribute("packetType", "");
        if ("notice".equals(message.getUserName())) {
            inflater.inflate(R.layout.ease_row_received_message, this);
        } else if ((!ObjectUtils.isNull(packetId)) &&
                (!ObjectUtils.isNull(packetType))) {
            //红包消息
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_red_package : R.layout.ease_row_sent_red_package, this);
        } else {
            if (ObjectUtils.isNull(message.getStringAttribute("packetId", ""))) {
                inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                        R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
            } else {
                inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                        R.layout.ease_row_received_red_package : R.layout.ease_row_sent_red_package, this);
            }
        }

        Log.i("CCCC", "type=======================" + message.getStringAttribute("type", ""));
        Log.i("CCCC", "approveId=======================" + message.getStringAttribute("approveId", ""));
        Log.i("CCCC", "getFrom=======================" + message.getFrom());
        Log.i("CCCP", "message=======================" + message.getStringAttribute("message", ""));
        Log.i("CCCP", "message.getStringAttribute(\"title\",\"\")=======================" + message.getStringAttribute("title", ""));
        type = message.getStringAttribute("type", "");
        approveId = message.getStringAttribute("approveId", "");

    }

    @Override
    protected void onFindViewById() {
        String packetId = message.getStringAttribute("packetId", "");
        String packetType = message.getStringAttribute("packetType", "");

        if ("notice".equals(message.getUserName())) {
            contentView = (TextView) findViewById(R.id.tv_chatcontent);
            iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
            tv_userid = (TextView) findViewById(R.id.tv_userid);
            approval_notify = (LinearLayout) findViewById(R.id.ll_approval_notify);//审批的布局
            approvalName = (TextView) findViewById(R.id.tv_approval_name);
            approvalTime = (TextView) findViewById(R.id.tv_apporva_time);
            approvalNumber = (TextView) findViewById(R.id.tv_approva_number);
            textAll = (RelativeLayout) findViewById(R.id.rl_text_all);//原text的总布局

            approval_notify.setVisibility(VISIBLE);
            approval_notify.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type != null) {
                        switch (type) {
                            case "101"://采购审核
                                intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                                intent.putExtra("approveId", approveId);
                                context.startActivity(intent);
                                break;
                            case "102"://采购审核结果
                                intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                                intent.putExtra("approveId", approveId);
                                context.startActivity(intent);
                                break;
                            case "201"://报销审核
                                intent = new Intent(context, ApprovalApplyDetailsActivity.class);
                                intent.putExtra("approveId", approveId);
                                context.startActivity(intent);
                                break;
                            case "202"://报销审核结果
                                intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                                context.startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                }
            });
            textAll.setVisibility(GONE);
        } else if ((!ObjectUtils.isNull(packetId)) &&
                (!ObjectUtils.isNull(packetType))) {
            tv_from = (TextView) findViewById(R.id.tv_from);
            tv_chatcontent = (TextView) findViewById(R.id.tv_chatcontent);
            contentView = (TextView) findViewById(R.id.tv_chatcontent);
            iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
            tv_userid = (TextView) findViewById(R.id.tv_userid);
        } else {
            textAll = (RelativeLayout) findViewById(R.id.rl_text_all);//原text的总布局
            //approval_notify = (LinearLayout) findViewById(R.id.ll_approval_notify);//审批的布局
            contentView = (TextView) findViewById(R.id.tv_chatcontent);
            iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
            tv_userid = (TextView) findViewById(R.id.tv_userid);
            bubble = (RelativeLayout) findViewById(R.id.bubble);
            //approval_notify.setVisibility(GONE);
            //textAll.setVisibility(VISIBLE);
        }

    }

    @Override
    public void onSetUpView() {
        String packetId = message.getStringAttribute("packetId", "");
        String packetType = message.getStringAttribute("packetType", "");
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        if ("notice".equals(message.getUserName())) {
            approvalName.setText(message.getStringAttribute("title", ""));

            Date date = new Date(message.getMsgTime());
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
            String LgTime = sdformat.format(date);
            approvalTime.setText(LgTime + "");
            approvalNumber.setText(message.getStringAttribute("message", ""));
        } else if ((!ObjectUtils.isNull(packetId)) &&
                (!ObjectUtils.isNull(packetType))) {
            String iconUrl = message.getStringAttribute("iconUrl", "");
            String nickName = message.getStringAttribute("nickName", "");
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
            // 设置内容
            String msg = ((EMTextMessageBody) message.getBody()).getMessage();
            //得到]的下标
            int position = msg.indexOf("]");
            if (!ObjectUtils.isNull(msg) && !ObjectUtils.isNull(position)) {
                tv_chatcontent.setText(msg.substring(position + 1, msg.length()));
                if (!ObjectUtils.isNull(packetType)) {
                    tv_from.setText(packetType);
                }
            }

        } else {
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

            Log.i("CCCC", "iconUrl=============================================" + iconUrl);
            Log.i("CCCC", "nickName=============================================" + nickName);

            //如果是印信就写死名称和头像========================================================================
            Log.i("CCCC", "印家的Username=============================================" + message.getUserName());
            if (message.getUserName().equals("secretary")) {
                if (message.direct() == EMMessage.Direct.SEND) {
                    Glide.with(getContext())
                            .load(iconUrl)
                            .transform(new CircleTransform(getContext()))
                            .into(iv_userhead);
                } else {
                    iv_userhead.setImageResource(R.drawable.secretary);
                }
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

        Log.i("CMCC", "=======================" + message.getStringAttribute("type", ""));
        Log.i("CMCC", "=======================" + message.getFrom());
        String packetId = message.getStringAttribute("packetId", "");
        String packetType = message.getStringAttribute("packetType", "");

        if ((!ObjectUtils.isNull(packetId)) &&
                (!ObjectUtils.isNull(packetType))) {
            //刷新一下防止错乱
            adapter.notifyDataSetChanged();
            Log.d("CMCC", "点击了红包packetId:" + packetId + ",packetType:" + packetType);
            // 红包弹出来dialog
            if (!ObjectUtils.isNull(packetId)) {
                lingQuRenId = message.getStringAttribute("userId", "");
                //判断红包状态
                String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                        "loginToken");
                if (ChatType.GroupChat == message.getChatType() ||
                        ChatType.ChatRoom == message.getChatType()) {

                    if (context.getString(R.string.group_common_Red_package).equals(packetType)) {
                        //普通红包(直接展示)
                        Log.d("CMCC", "普通红包!!");
                        DialogUtils.showProgressDialog(getContext(), "正在加载").show();
                        ChatRequest.getCommonPackageDetail(getContext(), token,
                                message.getTo(), "", packetId, commonCallBack);
                    } else if (context.getString(R.string.group_lucky_Red_package).equals(packetType)) {
                        Log.d("CMCC", "手气红包!!");
                        DialogUtils.showProgressDialog(getContext(), "正在加载").show();
                        ChatRequest.getLuckyPackageDetail(getContext(), token,
                                message.getTo(), "", packetId, luckyCallBack);
                    }
//                    if (1 == message.getIntAttribute("groupType", -1)) {
//                        Log.d("CMCC", "普通红包!!");
//                        DialogUtils.showProgressDialog(getContext(), "正在加载").show();
//                        ChatRequest.getCommonPackageDetail(getContext(), token,
//                                message.getTo(), "", packetId, commonCallBack);
//                    }//手气红包(直接展示)
//                    else if (2 == message.getIntAttribute("groupType", -1)) {
//                        Log.d("CMCC", "手气红包!!");
//                        DialogUtils.showProgressDialog(getContext(), "正在加载").show();
//                        ChatRequest.getLuckyPackageDetail(getContext(), token,
//                                message.getTo(), "", packetId, luckyCallBack);
//                    }
                } else {
                    Log.d("CMCC", "单聊红包!!");
                    //私聊查询红包记录
                    ChatRequest.queryPackageDetail(getContext(), token, packetId, callBack);
                }
            }
        } else if (type != null) {
            switch (type) {
                case "101"://采购审核
                    Intent intent1 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                    context.startActivity(intent1);
                    break;
                case "102"://采购审核结果
                    Intent intent2 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                    context.startActivity(intent2);
                    break;
                case "201"://报销审核
                    Intent intent3 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                    context.startActivity(intent3);
                    break;
                case "202"://报销审核结果
                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
                    context.startActivity(intent4);
                    break;
                case "301"://注册通知
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "302"://加好友通知
                    Intent intent5 = new Intent(context, NewFriendActivity.class);
                    context.startActivity(intent5);
                    break;
                case "401"://普通红包
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "402"://群红包
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "501"://加群审核
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "502"://进群通知
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "503"://退群通知
//                    Intent intent4 = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent4);
                    break;
                case "504"://群解散
                    break;
                case "601"://普通点对点消息对发
                    break;
                default:
                    break;
            }
        }
    }

    PackageDetailCallBack callBack = new PackageDetailCallBack() {
        @Override
        public void success(String msg, RedPackageDetail detail) {

            Log.d("detail", "------------->" + detail);

            if (!ObjectUtils.isNull(detail)) {


                handleSingleRedPackage(detail);


            }
        }

        @Override
        public void fail(String msg) {
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            Log.d("CMCC", "connectFail");
        }
    };

    /**
     * 处理单聊红包
     *
     * @param detail
     */
    private void handleSingleRedPackage(RedPackageDetail detail) {

//        Log.d("CMCC", "detail.getSendMemberId()---" + detail.getSendMemberId());
//        Log.d("CMCC", "lingQuRenId---" + detail.getSendMemberId());

        //判断有没有失效
        if (detail.isInvalid()) {
            //已失效
            goneDialog = new GoneRedEnvelopesDialog(context, R.style.MyDialog);
            goneDialog.setImgUrl(message.getStringAttribute("iconUrl", ""));
            goneDialog.setClickListener(new FailureRedEnvelopesListener() {
                @Override
                public void checkDetail() {
                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                    intent.putExtra("easemobGroupId", message.getTo());
                    intent.putExtra("type", 1001);
                    intent.putExtra("singleType", true);
                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                    context.startActivity(intent);
                }

                @Override
                public void closeDialog() {
                    goneDialog.dismiss();
                }
            });
            goneDialog.show();
        } else {
            //未失效
            String memberId = SharedPreferencesUtils.getShareString(context, ConFig.SHAREDPREFERENCES_NAME,
                    "memberId");
            if (detail.getSendMemberId().equals(memberId)) {
                //直接进入详情
                Intent intent = new Intent(context, RedPackageRecordActivity.class);
                intent.putExtra("easemobGroupId", message.getTo());
                intent.putExtra("type", 1001);
                intent.putExtra("singleType", true);
                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                context.startActivity(intent);
            } else {
                if (detail.isSnatch()) {

                    // snatch  true 已抢  false 未抢   //查看红包
                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                    intent.putExtra("easemobGroupId", message.getTo());
                    intent.putExtra("type", 1001);
                    intent.putExtra("singleType", true);
                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                    context.startActivity(intent);
                } else {
                    DialogUtils.showSinglePackageDialog(getContext(),
                            detail.getMasterFaceUrl(), detail.getMasterName(),
                            detail.getRemark(), detail.isInvalid(), detail.isSnatch(),
                            new DialogUtils.SinglePackageDialogCallBack() {
                                @Override
                                public void open() {
                                    //抢红包
                                    String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                                            "loginToken");
                                    DialogUtils.showProgressDialog(getContext(), "加载中").show();
                                    ChatRequest.receivePackage(getContext(), token,
                                            message.getStringAttribute("packetId", ""), receiveCallBack);
                                }

                            }).show();
                }
            }
        }

    }

    /**
     * 处理群手气红包
     *
     * @param detail
     */

    private void handleGroupLuckyRedPackage(GroupLuckyPackageDetail detail) {

        if (!ObjectUtils.isNull(detail.getSnatch()) && "true".equals(detail.getSnatch())) {
            // snatch  true 已抢  false 未抢   //查看红包
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("singleType", false);
            intent.putExtra("type", 2);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
        } else {
            DialogUtils.showGroupLuckyPackageDialog(getContext(),
                    detail.getSendMemberUrl(), detail.getSendMemberName(),
                    detail.getRemark(), detail.getInvalid(), detail.getSnatch(),
                    new DialogUtils.PackageDialogCallBack() {
                        @Override
                        public void open() {
                            //抢红包
                            String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                                    "loginToken");
                            DialogUtils.showProgressDialog(getContext(), "加载中").show();
                            ChatRequest.robRedPackage(getContext(), token, message.getTo(),
                                    "", message.getStringAttribute("packetId", ""), robPackageCallBack);
                        }

                        @Override
                        public void look() {
                            //查看红包
                            Intent intent = new Intent(context, RedPackageRecordActivity.class);
                            intent.putExtra("easemobGroupId", message.getTo());
                            intent.putExtra("singleType", false);
                            intent.putExtra("type", 2);
                            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                            context.startActivity(intent);
                        }
                    }).show();
        }
    }


    //单人抢红包
    ReceivedPackageCallBack receiveCallBack = new ReceivedPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //发透传消息的
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", 1001);
            intent.putExtra("singleType", true);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };
    //群普通红包查询
    GetCommonPackageDetailCallBack commonCallBack = new GetCommonPackageDetailCallBack() {

        @Override
        public void success(String msg, CommonPackage detail) {
            Log.d("CMCC", "-------CommonPackage----" + detail);
            DialogUtils.closeProgressDialog();
            groupDialog = new GroupRedEnvelopesDialog(getContext(), R.style.MyDialog);
            groupDialog.setMoneryNum(Double.parseDouble(detail.getAmount()));
            if (ObjectUtils.isNull(message.getStringAttribute("nickName", ""))) {
                groupDialog.setRedPackageSender(message.getStringAttribute("userId", ""));
            } else {
                groupDialog.setRedPackageSender(message.getStringAttribute("nickName", ""));
            }
            groupDialog.setImgUrl(message.getStringAttribute("iconUrl", ""));
            groupDialog.setLeaveMsg(detail.getRemark());
            groupDialog.setClickListener(new GroupRedEnvelopesListener() {
                @Override
                public void closeDialog() {
                    groupDialog.dismiss();
                }
            });
            groupDialog.show();
            //TODO
            //发透传消息的
            //跳转到详情页
//            Intent intent = new Intent(context, RedPackageRecordActivity.class);
//            intent.putExtra("easemobGroupId", message.getTo());
//            intent.putExtra("type", message.getIntAttribute("type", -1));
//            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//            context.startActivity(intent);
//            singleDialog.dismiss();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };

    //群拼手气红包查询
    GetLuckyPackageDetailCallBack luckyCallBack = new GetLuckyPackageDetailCallBack() {

        @Override
        public void success(String msg, GroupLuckyPackageDetail detail) {
            Log.d("群拼手气红包查询", "---->" + detail.getList().size());
            DialogUtils.closeProgressDialog();
            handleGroupLuckyRedPackage(detail);
            //发透传消息的
            //跳转到详情页
//            Intent intent = new Intent(context, RedPackageRecordActivity.class);
//            intent.putExtra("easemobGroupId", message.getTo());
//            intent.putExtra("type", message.getIntAttribute("type", -1));
//            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//            context.startActivity(intent);
//            singleDialog.dismiss();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };

    RobPackageCallBack robPackageCallBack = new RobPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            // Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //发透传消息的
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", 2);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };
}
