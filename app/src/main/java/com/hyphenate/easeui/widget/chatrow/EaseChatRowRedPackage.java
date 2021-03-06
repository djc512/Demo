package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.chat.CommonPackage;
import huanxing_print.com.cn.printhome.model.chat.GroupLuckyPackageDetail;
import huanxing_print.com.cn.printhome.model.chat.RedPackageDetail;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
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
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.FailureRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GoneRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.GroupRedEnvelopesDialog;
import huanxing_print.com.cn.printhome.view.dialog.SingleRedEnvelopesDialog;

/**
 * description: 红包
 * author LSW
 * date 2017/5/17 22:19
 * update 2017/5/17
 */
public class EaseChatRowRedPackage extends EaseChatRowText {

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
    private String type;
    private String packetId;
    private TextView tv_from;
    private GroupLuckyPackageDetail packageDetail;

    public EaseChatRowRedPackage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_red_package : R.layout.ease_row_sent_red_package, this);
        type = message.getStringAttribute("type", "");
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        bubble = (RelativeLayout) findViewById(R.id.bubble);
        tv_from = (TextView) findViewById(R.id.tv_from);
    }

    @Override
    public void onSetUpView() {

        String packetType = message.getStringAttribute("packetType", "");
        // 设置内容
        String msg = ((EMTextMessageBody) message.getBody()).getMessage();
        //得到]的下标
        int position = msg.indexOf("]");
        if (!ObjectUtils.isNull(msg) && !ObjectUtils.isNull(position)) {
            contentView.setText(msg.substring(position + 1, msg.length()));
        } else {
            contentView.setText(msg);
        }
        if (!ObjectUtils.isNull(packetType)) {
            tv_from.setText(packetType);
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
        packetId = message.getStringAttribute("packetId", "");
        String packetType = message.getStringAttribute("packetType", "");
        Log.d("CMCC", "packetId:" + packetId);

        // 红包弹出来dialog
        if (!ObjectUtils.isNull(packetId)) {
            lingQuRenId = message.getStringAttribute("userId", "");
            //判断红包状态
            String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                    "loginToken");
            if (ChatType.GroupChat == message.getChatType() ||
                    ChatType.ChatRoom == message.getChatType()) {
                //普通红包(直接展示)
                if (context.getString(R.string.group_common_Red_package).equals(packetType)) {
                    //Log.d("CMCC", "普通红包!!");
                    DialogUtils.showProgressDialog(getContext(), "正在加载").show();
                    ChatRequest.getCommonPackageDetail(getContext(), token,
                            message.getTo(), "", packetId, commonCallBack);
                }//手气红包(直接展示)
                else if (context.getString(R.string.group_lucky_Red_package).equals(packetType)) {
                    //Log.d("CMCC", "手气红包!!");
                    DialogUtils.showProgressDialog(getContext(), "正在加载").show();
                    ChatRequest.getLuckyPackageDetail(getContext(), token,
                            message.getTo(), "", packetId, luckyCallBack);
                }
            } else {
                DialogUtils.showProgressDialog(getContext(), "正在加载").show();
                //私聊查询红包记录
                ChatRequest.queryPackageDetail(getContext(), token, packetId, callBack);
            }
        }
    }

    PackageDetailCallBack callBack = new PackageDetailCallBack() {
        @Override
        public void success(String msg, RedPackageDetail detail) {
            DialogUtils.closeProgressDialog();
            Log.d("detail", "------------->" + detail);

            if (!ObjectUtils.isNull(detail)) {
                handleSingleRedPackage(detail);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(context, "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
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

        //判断是不是他本人发的红包
        String memberId = SharedPreferencesUtils.getShareString(context, ConFig.SHAREDPREFERENCES_NAME,
                "memberId");
        if (memberId.equals(message.getStringAttribute("userId", ""))) {
            //直接跳到详情界面
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", 1001);
            intent.putExtra("singleType", true);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);
        } else {
            Log.d("CMCC", "invalid:" + detail.getInvalid());
            //判断有没有失效
            if ("true".equals(detail.getInvalid())) {
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
                        goneDialog.dismiss();
                    }

                    @Override
                    public void closeDialog() {
                        goneDialog.dismiss();
                    }
                });
                goneDialog.show();
            } else {
                Log.d("CMCC", "isSnatch:" + detail.getSnatch());
                //未失效  判断是否已经抢过
                if ("true".equals(detail.getSnatch())) {
                    // snatch  true 已抢  false 未抢   //查看红包
                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                    intent.putExtra("easemobGroupId", message.getTo());
                    intent.putExtra("type", message.getIntAttribute("groupType", -1));
                    intent.putExtra("singleType", true);
                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                    context.startActivity(intent);
                } else {
                    singleDialog = new SingleRedEnvelopesDialog(context, R.style.MyDialog);
                    singleDialog.setRedPackageSender(detail.getMasterName());
                    singleDialog.setImgUrl(detail.getMasterFaceUrl());
                    singleDialog.setLeaveMsg(detail.getRemark());
                    singleDialog.setCheckDetail(false);
                    singleDialog.setClickListener(new NormalRedEnvelopesListener() {
                        @Override
                        public void open() {
                            //抢红包
                            String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                                    "loginToken");
                            DialogUtils.showProgressDialog(getContext(), "加载中").show();
                            ChatRequest.receivePackage(getContext(), token,
                                    message.getStringAttribute("packetId", ""), receiveCallBack);
                            singleDialog.dismiss();
                        }

                        @Override
                        public void checkDetail() {
                            Intent intent = new Intent(context, RedPackageRecordActivity.class);
                            intent.putExtra("easemobGroupId", message.getTo());
                            intent.putExtra("type", message.getIntAttribute("groupType", -1));
                            intent.putExtra("singleType", true);
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
//                    DialogUtils.showSinglePackageDialog(getContext(),
//                            detail.getMasterFaceUrl(), detail.getMasterName(),
//                            detail.getRemark(), detail.getInvalid(), detail.getSnatch(),
//                            new DialogUtils.SinglePackageDialogCallBack() {
//                                @Override
//                                public void open() {
//
//                                }
//
//                            }).show();
                }
//                if (detail.getSendMemberId().equals(memberId)) {
//                    //直接进入详情
//                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
//                    intent.putExtra("easemobGroupId", message.getTo());
//                    intent.putExtra("type", message.getIntAttribute("groupType", -1));
//                    intent.putExtra("singleType", true);
//                    intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
//                    context.startActivity(intent);
//                } else {
//
//                }
            }
        }


    }

    /**
     * 处理群手气红包
     *
     * @param detail
     */

    private void handleGroupLuckyRedPackage(GroupLuckyPackageDetail detail) {
        packageDetail = detail;
        //判断是否已经失效
        if (!ObjectUtils.isNull(detail.isInvalid())) {
            if (detail.isInvalid()) {
                //失效  展示失效界面
                dialog = new FailureRedEnvelopesDialog(context, R.style.MyDialog);
                dialog.setRedPackageSender(detail.getSendMemberName());
                dialog.setImgUrl(detail.getSendMemberUrl());
                dialog.setClickListener(new FailureRedEnvelopesListener() {
                    @Override
                    public void checkDetail() {
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("singleType", false);
                        intent.putExtra("type", 2);
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
            } else {
                Log.d("CMCC", "");
                //未失效 判断是否抢过
                if (!ObjectUtils.isNull(detail.isSnatch())) {
                    if (detail.isSnatch()) {
                        //已经抢过了 到详情界面
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("singleType", false);
                        intent.putExtra("type", 2);
                        intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
                        context.startActivity(intent);
                    } else {
                        //未抢过 判断有没有抢完
                        if (Integer.parseInt(detail.getSnatchNum()) < Integer.parseInt(detail.getTotalNumber())) {
                            //没抢完
                            singleDialog = new SingleRedEnvelopesDialog(context, R.style.MyDialog);
                            singleDialog.setImgUrl(detail.getSendMemberUrl());
                            singleDialog.setRedPackageSender(detail.getSendMemberName());
                            singleDialog.setLeaveMsg(detail.getRemark());
                            singleDialog.setClickListener(new NormalRedEnvelopesListener() {
                                @Override
                                public void open() {
                                    //抢红包
                                    String token = SharedPreferencesUtils.getShareString(getContext(), ConFig.SHAREDPREFERENCES_NAME,
                                            "loginToken");
                                    DialogUtils.showProgressDialog(getContext(), "加载中").show();
                                    ChatRequest.robRedPackage(getContext(), token, message.getTo(),
                                            "", message.getStringAttribute("packetId", ""), robPackageCallBack);
                                    singleDialog.dismiss();
                                }

                                @Override
                                public void checkDetail() {
                                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                                    intent.putExtra("easemobGroupId", message.getTo());
                                    intent.putExtra("singleType", false);
                                    intent.putExtra("type", 2);
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
                        } else {
                            //已经抢完
                            goneDialog = new GoneRedEnvelopesDialog(context, R.style.MyDialog);
                            goneDialog.setRedPackageSender(detail.getSendMemberName());
                            goneDialog.setImgUrl(detail.getSendMemberUrl());
                            goneDialog.setClickListener(new FailureRedEnvelopesListener() {
                                @Override
                                public void checkDetail() {
                                    Intent intent = new Intent(context, RedPackageRecordActivity.class);
                                    intent.putExtra("easemobGroupId", message.getTo());
                                    intent.putExtra("singleType", false);
                                    intent.putExtra("type", 2);
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
                        }
                    }
                }
            }
        }

    }


    //单人抢红包
    ReceivedPackageCallBack receiveCallBack = new ReceivedPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //更新余额
            RefreshEvent event = new RefreshEvent();
            event.setCode(0x11);
            EventBus.getDefault().post(event);
            //跳转到详情页
            Intent intent = new Intent(context, RedPackageRecordActivity.class);
            intent.putExtra("easemobGroupId", message.getTo());
            intent.putExtra("type", message.getIntAttribute("groupType", -1));
            intent.putExtra("singleType", true);
            intent.putExtra("packetId", message.getStringAttribute("packetId", ""));
            context.startActivity(intent);

        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(context, "" + msg);
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
            groupDialog.setMoneryNum(detail.getAmount());
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
            ToastUtil.doToast(context, "" + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
        }
    };

    RobPackageCallBack robPackageCallBack = new RobPackageCallBack() {
        @Override
        public void success(String msg, String amount) {
            // Log.d("CMCC", "" + msg + "," + amount);
            DialogUtils.closeProgressDialog();
            //更新余额
            RefreshEvent event = new RefreshEvent();
            event.setCode(0x11);
            EventBus.getDefault().post(event);

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
            //弹出已经抢完dialog
            if (!ObjectUtils.isNull(packageDetail)) {
                Log.d("CMCC", "没抢到!!");

                goneDialog = new GoneRedEnvelopesDialog(context, R.style.MyDialog);
                goneDialog.setRedPackageSender(packageDetail.getSendMemberName());
                goneDialog.setImgUrl(packageDetail.getSendMemberUrl());
                goneDialog.setClickListener(new FailureRedEnvelopesListener() {
                    @Override
                    public void checkDetail() {
                        Intent intent = new Intent(context, RedPackageRecordActivity.class);
                        intent.putExtra("easemobGroupId", message.getTo());
                        intent.putExtra("singleType", false);
                        intent.putExtra("type", 2);
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
            }
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
        }
    };
}
