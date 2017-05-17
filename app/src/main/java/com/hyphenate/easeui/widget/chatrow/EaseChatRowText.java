package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.util.Log;
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
import huanxing_print.com.cn.printhome.ui.activity.yinxin.RedPackageRecordActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
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

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    private ImageView iv_userhead;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private FailureRedEnvelopesDialog dialog;
    private SingleRedEnvelopesDialog singleDialog;
    private GroupRedEnvelopesDialog groupDialog;
    private GoneRedEnvelopesDialog goneDialog;
    private String lingQuRenNickName;//发红包人的昵称
    private String lingQuRenId;//发红包人的id

    private String packetId;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if (ObjectUtils.isNull(message.getStringAttribute("packetId", ""))) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
        } else {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.ease_row_received_red_package : R.layout.ease_row_sent_red_package, this);
        }

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
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

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
         packetId = message.getStringAttribute("packetId", "");
        Log.d("packetId", "-------------->" + packetId);
        // 红包弹出来dialog
        if (!ObjectUtils.isNull(packetId)) {
            lingQuRenId = message.getStringAttribute("userId", "");
            //判断红包状态
            String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");
//            Log.d("CMCC", "--------type------>" + message.getIntAttribute("groupType", -1));
//            Log.d("CMCC", "--------groupID------>" +  message.getTo());
            if (ChatType.GroupChat == message.getChatType() ||
                    ChatType.ChatRoom == message.getChatType()) {
                //普通红包(直接展示)
                if (1 == message.getIntAttribute("groupType", -1)){
                    DialogUtils.showProgressDialog(getContext(),"正在加载").show();
                    ChatRequest.getCommonPackageDetail(getContext(), token,
                            message.getTo(),message.getTo(),packetId,commonCallBack);
                }//手气红包(直接展示)
                else if (2 == message.getIntAttribute("groupType", -1)) {
                    DialogUtils.showProgressDialog(getContext(),"正在加载").show();
                    ChatRequest.getLuckyPackageDetail(getContext(), token,
                            message.getTo(),"",packetId,luckyCallBack);
            }


//                lingQuRenNickName = detail.getMasterName();
//                //群聊
//                handleGroupRedPackage(detail);


            } else {
                //私聊查询红包记录
                ChatRequest.queryPackageDetail(getContext(), token, packetId,callBack);
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
        //判断状态
        if (!detail.isInvalid()) {
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
            if (detail.getSendMemberId().equals(lingQuRenId)){
                //直接进入详情
                Intent intent = new Intent(context, RedPackageRecordActivity.class);
                intent.putExtra("easemobGroupId", message.getTo());
                intent.putExtra("type", message.getIntAttribute("groupType", -1));
                intent.putExtra("singleType", true);
                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                context.startActivity(intent);
            }else{

                singleDialog = new SingleRedEnvelopesDialog(getContext());
                singleDialog.setImgUrl(detail.getMasterFaceUrl());
                singleDialog.setLeaveMsg(detail.getRemark());
                singleDialog.setRedPackageSender(detail.getMasterName());
                singleDialog.setClickListener(new NormalRedEnvelopesListener(){

                    @Override
                    public void open() {
                        //抢红包
                        String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                                "loginToken");

                        ChatRequest.receivePackage(getContext(),token,packetId,receiveCallBack);
                    }

                    @Override
                    public void checkDetail() {

                    }

                    @Override
                    public void closeDialog() {

                    }
                });
            }

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
                    intent.putExtra("type", message.getIntAttribute("groupType", -1));
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
     * 处理群手气红包
     *
     * @param detail
     */

    private void handleGroupLuckyRedPackage(GroupLuckyPackageDetail detail) {

//        //手气红包
//        singleDialog = new SingleRedEnvelopesDialog(getContext());
//        singleDialog.setLeaveMsg(detail.getRemark());
//        singleDialog.setRedPackageSender(detail.getSendMemberName());
//        singleDialog.setImgUrl(detail.getSendMemberUrl());
//        singleDialog.setClickListener(new NormalRedEnvelopesListener() {
//            @Override
//            public void open() {
//                //抢红包
//                String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
//                        "loginToken");
//                DialogUtils.showProgressDialog(getContext(), "加载中").show();
//                ChatRequest.robRedPackage(getContext(), token, message.getTo(),
//                        "", message.getStringAttribute("packetId", ""), robPackageCallBack);
//            }
//
//            @Override
//            public void checkDetail() {
//                //查看红包详情
//                Intent intent = new Intent(context, RedPackageRecordActivity.class);
//                intent.putExtra("easemobGroupId", message.getTo());
//                intent.putExtra("type", message.getIntAttribute("groupType", -1));
//                intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//                context.startActivity(intent);
//                singleDialog.dismiss();
//            }
//
//            @Override
//            public void closeDialog() {
//                singleDialog.dismiss();
//            }
//        });
//        singleDialog.show();

        DialogUtils.showPackageDialog(getContext(),
                detail.getSendMemberUrl(),detail.getSendMemberName(),
                detail.getRemark(),detail.getInvalid(),detail.getSnatch(),
                new DialogUtils.PackageDialogCallBack(){
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
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("type", message.getIntAttribute("groupType", -1));
                        intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                        context.startActivity(intent);
                    }
                }).show();

    }

    private void handleGroupRedPackage(RedPackageDetail detail) {
        //判断状态
        if (!detail.isInvalid()) {
            //有效(再判断有没有抢过)
            if (detail.isSnatch()) {
                goneDialog = new GoneRedEnvelopesDialog(getContext());
                goneDialog.setImgUrl(detail.getMasterFaceUrl());
                goneDialog.setRedPackageSender(detail.getMasterName());
                goneDialog.setClickListener(new FailureRedEnvelopesListener() {
                    @Override
                    public void checkDetail() {
                        //已经抢过了直接跳到详情页面
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("type", message.getIntAttribute("groupType", -1));
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
                    if (1 == message.getIntAttribute("groupType", -1)) {
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
                    } else if (2 == message.getIntAttribute("groupType", -1)) {
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
                                intent.putExtra("type", message.getIntAttribute("groupType", -1));
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
                    intent.putExtra("type", message.getIntAttribute("groupType", -1));
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
     //单人抢红包
    ReceivedPackageCallBack receiveCallBack = new ReceivedPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            Log.d("CMCC", "" + msg + "," + amount);
            //DialogUtils.closeProgressDialog();
            //发透传消息的
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", message.getIntAttribute("groupType", -1));
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
            singleDialog.dismiss();
        }

        @Override
        public void fail(String msg) {
            //DialogUtils.closeProgressDialog();
            Log.d("CMCC", "" + msg);
        }

        @Override
        public void connectFail() {
            //DialogUtils.closeProgressDialog();
            Log.d("CMCC", "connectFail");
        }
    };
//群普通红包查询
GetCommonPackageDetailCallBack commonCallBack = new GetCommonPackageDetailCallBack() {

    @Override
        public void success(String msg, CommonPackage detail) {
        Log.d("CMCC", "-------CommonPackage----"+detail);
            DialogUtils.closeProgressDialog();
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
            //Log.d("detail", "---->" + detail);
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
            Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //发透传消息的
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", message.getIntAttribute("groupType", -1));
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
