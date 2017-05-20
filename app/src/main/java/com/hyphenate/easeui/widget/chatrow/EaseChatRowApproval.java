package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.chat.RedPackageDetail;
import huanxing_print.com.cn.printhome.net.callback.chat.PackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.RobPackageCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalApplyDetailsActivity;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalBuyAddOrRemoveActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;
import huanxing_print.com.cn.printhome.ui.activity.yinxin.RedPackageRecordActivity;
import huanxing_print.com.cn.printhome.util.FailureRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.GroupRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.NormalRedEnvelopesListener;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.FailureRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GoneRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GroupRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.SingleRedEnvelopesDialog;

public class EaseChatRowApproval extends EaseChatRowText {

    private TextView approvalName;
    private TextView approvalTime;
    private TextView approvalNumber;
    private RelativeLayout bubble;
    private FailureRedEnvelopesDialog dialog;
    private SingleRedEnvelopesDialog singleDialog;
    private GroupRedEnvelopesDialog groupDialog;
    private GoneRedEnvelopesDialog goneDialog;
    private String lingQuRenNickName;//发红包人的昵称
    private String lingQuRenId;//发红包人的id
    private String type;
    private String approveId;
    private Intent intent;

    public EaseChatRowApproval(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        String type = message.getStringAttribute("type", "");
        inflater.inflate(R.layout.raw_approval_chat, this);

    }

    @Override
    protected void onFindViewById() {
        approvalName = (TextView) findViewById(R.id.tv_approval_name);
        approvalTime = (TextView) findViewById(R.id.tv_apporva_time);
        approvalNumber = (TextView) findViewById(R.id.tv_approva_number);
        bubble = (RelativeLayout) findViewById(R.id.bubble);
    }

    @Override
    public void onSetUpView() {

        /*EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        String iconUrl = message.getStringAttribute("iconUrl", "");
        String nickName = message.getStringAttribute("nickName", "");*/

        //如果是印信就写死名称和头像========================================================================
        Log.i("CCCC", "印家的Username=============================================" + message.getUserName());
        approvalName.setText(message.getStringAttribute("title", ""));
        approvalNumber.setText(message.getStringAttribute("message", ""));
        //approvalName.setText(message.getStringAttribute("",""));


        handleTextMessage();
    }

    protected void handleTextMessage() {

        /*if (message.direct() == EMMessage.Direct.SEND) {
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
        }*/
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

        Log.i("CCCC", "=======================" + message.getStringAttribute("type", ""));
        Log.i("CCCC", "=======================" + message.getFrom());
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
                    intent = new Intent(context, ApprovalApplyDetailsActivity.class);
                    context.startActivity(intent);
                    break;
                case "301"://注册通知
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "302"://加好友通知
                    intent = new Intent(context, NewFriendActivity.class);
                    context.startActivity(intent);
                    break;
                case "401"://普通红包
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "402"://群红包
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "501"://加群审核
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "502"://进群通知
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "503"://退群通知
//                    intent = new Intent(context, ApprovalBuyAddOrRemoveActivity.class);
//                    context.startActivity(intent);
                    break;
                case "504"://群解散
                    break;
                case "601"://普通点对点消息对发
                    break;
                default:
                    break;
            }
        }

        // 红包弹出来dialog
        if (!ObjectUtils.isNull(message.getStringAttribute("packetId", ""))) {
            lingQuRenId = message.getStringAttribute("userId", "");
            //判断红包状态
            String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");
            ChatRequest.queryPackageDetail(getContext(), token, message.getStringAttribute("packetId", ""),
                    callBack);
        }
    }

    PackageDetailCallBack callBack = new PackageDetailCallBack() {
        @Override
        public void success(String msg, RedPackageDetail detail) {
            Log.d("CMCC", "" + msg);
            if (!ObjectUtils.isNull(detail)) {
                if (ChatType.GroupChat == message.getChatType() ||
                        ChatType.ChatRoom == message.getChatType()) {
                    lingQuRenNickName = detail.getMasterName();
                    //群聊
                    handleGroupRedPackage(detail);
                } else {
                    //私聊
                    handleSingleRedPackage(detail);
                }
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
        //判断状态
        if ("true".equals(detail.getInvalid())) {
//            //有效(再判断有没有抢过)
//            if (detail.isSnatch()) {
//                //已经抢过了
//                //查看红包领取详情(区分一下type)
//                Intent intent = new Intent(context, RedPackageRecordActivity.class);
//                intent.putExtra("easemobGroupId", message.getTo());
//                intent.putExtra("type", message.getIntAttribute("type", -1));
//                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//                context.startActivity(intent);
//            } else {
//                //直接进入详情
//                Intent intent = new Intent(context, RedPackageRecordActivity.class);
//                intent.putExtra("easemobGroupId", message.getTo());
//                intent.putExtra("type", message.getIntAttribute("type", -1));
//                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//                context.startActivity(intent);
//            }
            //直接进入详情
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", message.getIntAttribute("type", -1));
            intent.putExtra("singleType", true);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
        } else {
            //失效
            dialog = new FailureRedEnvelopesDialog(getContext());
            dialog.setImgUrl(detail.getFaceUrl());
            dialog.setRedPackageSender(detail.getMasterName());
            dialog.setClickListener(new FailureRedEnvelopesListener() {
                @Override
                public void checkDetail() {
                    //查看红包领取详情(区分一下type)
                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                    intent.putExtra("easemobGroupId", message.getTo());
                    intent.putExtra("type", message.getIntAttribute("type", -1));
                    intent.putExtra("singleType", true);
                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                    context.startActivity(intent);
                    dialog.dismiss();
                }

                @Override
                public void closeDialog() {
                    dialog.dismiss();
                }
            });
        }
    }

    /**
     * 处理群红包
     *
     * @param detail
     */
    private void handleGroupRedPackage(RedPackageDetail detail) {
        //判断状态
        if ("true".equals(detail.getInvalid())) {
            //有效(再判断有没有抢过)
            if ("true".equals(detail.getSnatch())) {
                goneDialog = new GoneRedEnvelopesDialog(getContext());
                goneDialog.setImgUrl(detail.getMasterFaceUrl());
                goneDialog.setRedPackageSender(detail.getMasterName());
                goneDialog.setClickListener(new FailureRedEnvelopesListener() {
                    @Override
                    public void checkDetail() {
                        //已经抢过了直接跳到详情页面
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("type", message.getIntAttribute("type", -1));
                        intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                        context.startActivity(intent);
                        goneDialog.dismiss();
                    }

                    @Override
                    public void closeDialog() {
                        goneDialog.dismiss();
                    }
                });
                goneDialog.show();
            } else {
                //未抢过
                //这里区分一下群普通红包还是手气红包
                if (ObjectUtils.isNull(message.getIntAttribute("", -1))) {
                    if (1 == message.getIntAttribute("type", -1)) {
                        //普通红包(直接展示)
                        groupDialog = new GroupRedEnvelopesDialog(getContext());
                        groupDialog.setImgUrl(detail.getMasterFaceUrl());
                        groupDialog.setLeaveMsg(detail.getRemark());
                        groupDialog.setRedPackageSender(detail.getMasterName());
                        groupDialog.setMoneryNum(Double.parseDouble(detail.getAmount()));
                        groupDialog.setClickListener(new GroupRedEnvelopesListener() {
                            @Override
                            public void closeDialog() {
                                groupDialog.dismiss();
                            }
                        });
                        groupDialog.show();
                    } else if (2 == message.getIntAttribute("type", -1)) {
                        //手气红包
                        singleDialog = new SingleRedEnvelopesDialog(getContext());
                        singleDialog.setLeaveMsg(detail.getRemark());
                        singleDialog.setRedPackageSender(detail.getMasterName());
                        singleDialog.setImgUrl(detail.getMasterFaceUrl());
                        singleDialog.setClickListener(new NormalRedEnvelopesListener() {
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
                            public void checkDetail() {
                                //查看红包详情
                                Intent intent = new Intent(context, RedPackageRecordActivity.class);
                                intent.putExtra("easemobGroupId", message.getTo());
                                intent.putExtra("type", message.getIntAttribute("type", -1));
                                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                                context.startActivity(intent);
                                singleDialog.dismiss();
                            }

                            @Override
                            public void closeDialog() {
                                singleDialog.dismiss();
                            }
                        });
                        singleDialog.show();
                    }
                }
            }
        } else {
            //失效
            dialog = new FailureRedEnvelopesDialog(getContext());
            dialog.setImgUrl(detail.getMasterFaceUrl());
            dialog.setRedPackageSender(detail.getMasterName());
            dialog.setClickListener(new FailureRedEnvelopesListener() {
                @Override
                public void checkDetail() {
                    //群红包才会有记录页面
                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                    intent.putExtra("easemobGroupId", message.getTo());
                    intent.putExtra("type", message.getIntAttribute("type", -1));
                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                    context.startActivity(intent);
                    dialog.dismiss();
                }

                @Override
                public void closeDialog() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    RobPackageCallBack robPackageCallBack = new RobPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //发透传消息的
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", message.getIntAttribute("type", -1));
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
            singleDialog.dismiss();
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
