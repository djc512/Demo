package huanxing_print.com.cn.printhome.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.model.print.GroupResp;
import huanxing_print.com.cn.printhome.ui.adapter.GroupRecylerAdapter;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.LinePathView;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

import static huanxing_print.com.cn.printhome.R.id.img_open;
import static huanxing_print.com.cn.printhome.R.id.tv_content;


public class DialogUtils {

    public static Dialog mProgressDialog;
    public static Dialog mShareDialog;
    public static Dialog mTipsDialog;
    public static Dialog mAuditStatusDialog;
    public static Dialog mLocationDialog;
    public static Dialog mSignatureDialog;
    public static Dialog mVersionDialog;
    public static Dialog mPicChooseDialog;
    public static Dialog mActivityDialog;
    public static Dialog mPackageDialog;
    public static Dialog mSinglePackageDialog;

    public interface ShareDialogCallBack {

        public void wechat();

        public void wechatMoment();

        public void cancel();

    }

    public interface TipsDialogCallBack {
        public void ok();
    }

    public interface PackageDialogCallBack {
        public void open();

        public void look();

    }

    public interface SinglePackageDialogCallBack {
        public void open();

    }

    public interface AuditStatusDialogCallBack {
        public void ok();

    }

    public interface VersionDialogCallBack {
        public void update();

    }

    public interface PicChooseDialogCallBack {
        public void camera();

        public void photo();

    }

    public interface PayChooseDialogCallBack {
        public void wechat();

        public void alipay();

    }

    public interface PayQunChooseDialogCallBack {
        public void tuniu();

        public void yinjia();

    }

    public interface QunOwnerTransferDialogCallBack {
        void transfer();
    }

    public interface QunOwnerDissolutionDialogCallBack {
        void dissolution();
    }

    public interface GroupDelMemDialogCallback {
        void del();
    }

    public interface ExitGroupDialogCallback {
        void exit();
    }

    public interface RedPackageCallback {
        void send();
    }

    public static Dialog showProgressDialog(Context context, String content) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_progress, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.transparent_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView textView = (TextView) mProgressDialog.findViewById(R.id.tv_info);
        textView.setText(content);
        return mProgressDialog;
    }

    public static Dialog showTipsDialog(Context context, String content, final TipsDialogCallBack tipsDialogCallBack) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tips, null);
        mTipsDialog = new Dialog(context, R.style.loading_dialog);
        mTipsDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mTipsDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mTipsDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mTipsDialog.getWindow().setAttributes(lp);
        mTipsDialog.setCanceledOnTouchOutside(true);

        Button okCancel = (Button) view.findViewById(R.id.btn_canncel);
        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
        TextView contentTv = (TextView) view.findViewById(tv_content);
        contentTv.setText(content);
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTipsDialog.dismiss();
                tipsDialogCallBack.ok();
            }
        });
        okCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTipsDialog.dismiss();

            }
        });

        return mTipsDialog;
    }

    public static Dialog showShareDialog(final Context context, final ShareDialogCallBack shareDialogCallBack) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_share, null);
        mShareDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        mShareDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = mShareDialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;
        mShareDialog.onWindowAttributesChanged(wl);
        mShareDialog.setCanceledOnTouchOutside(true);

        Button wechatMomentBtn = (Button) view.findViewById(R.id.invitation_pup__wechat);
        Button wechatBtn = (Button) view.findViewById(R.id.invitation_wechat);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_wechat);
        wechatMomentBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isWeixinAvilible(context)) {
                    shareDialogCallBack.wechatMoment();
                } else {
                    ToastUtil.doToast(context, "您还没有安装微信，请先安装微信客户端");
                }
                mShareDialog.dismiss();
            }
        });
        wechatBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtils.isWeixinAvilible(context)) {
                    shareDialogCallBack.wechat();
                } else {
                    ToastUtil.doToast(context, "您还没有安装微信，请先安装微信客户端");
                }
                mShareDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mShareDialog.dismiss();
                shareDialogCallBack.cancel();
            }
        });

        return mShareDialog;
    }

    public static Dialog showPicChooseDialog(Context context, final PicChooseDialogCallBack callBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pic_choose, null);
        mPicChooseDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        mPicChooseDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = mPicChooseDialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        mPicChooseDialog.onWindowAttributesChanged(wl);
        mPicChooseDialog.setCanceledOnTouchOutside(true);
        mPicChooseDialog.show();
        Button photoBtn = (Button) view.findViewById(R.id.gallery_pup);
        Button cameraBtn = (Button) view.findViewById(R.id.photograph_pup);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_pup);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();

            }
        });
        cameraBtn.setOnClickListener(new OnClickListener() {//拍照
            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();
                callBack.camera();
            }
        });

        photoBtn.setOnClickListener(new OnClickListener() {//从相传选择
            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();
                callBack.photo();
            }
        });
        return mPicChooseDialog;
    }

    /**
     * 支付选择对话框
     *
     * @param context
     * @param callBack
     * @return
     */
    public static Dialog showPayChooseDialog(Context context, String amount, final PayChooseDialogCallBack callBack) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null);
        mPicChooseDialog = new Dialog(context, R.style.paytransparentFrameWindowStyle);
        mPicChooseDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = mPicChooseDialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        mPicChooseDialog.onWindowAttributesChanged(wl);
        mPicChooseDialog.setCanceledOnTouchOutside(true);
        mPicChooseDialog.show();
        LinearLayout ll_wechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        LinearLayout ll_alipay = (LinearLayout) view.findViewById(R.id.ll_alipay);
        TextView amountTv = (TextView) view.findViewById(R.id.amountTv);
        amountTv.setText("￥" + amount);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();
            }
        });
        ll_wechat.setOnClickListener(new OnClickListener() {//微信支付
            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();
                callBack.wechat();
            }
        });

        ll_alipay.setOnClickListener(new OnClickListener() {//支付宝支付
            @Override
            public void onClick(View arg0) {
                mPicChooseDialog.dismiss();
                callBack.alipay();
            }
        });
        return mPicChooseDialog;
    }

    public static Dialog showQunChooseDialog(Context context, List<GroupResp.Group> groupList, GroupRecylerAdapter
            .OnItemClickListener onItemClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_qun1, null);
        mPicChooseDialog = new Dialog(context, R.style.paytransparentFrameWindowStyle);
        mPicChooseDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = mPicChooseDialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;
        mPicChooseDialog.onWindowAttributesChanged(wl);
        mPicChooseDialog.setCanceledOnTouchOutside(true);
        mPicChooseDialog.show();

        RecyclerView mRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        GroupRecylerAdapter mAdapter = new GroupRecylerAdapter(groupList);
        mRcList.setAdapter(mAdapter);
        mRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(context, R.color.devide_gray)));
        mAdapter.setOnItemClickListener(onItemClickListener);

//        RelativeLayout ll_yinjia = (RelativeLayout) view.findViewById(R.id.ll_yinjia);
//        RelativeLayout ll_tuniu = (RelativeLayout) view.findViewById(R.id.ll_tuniu);
//        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
//        tv_cancle.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                mPicChooseDialog.dismiss();
//            }
//        });
//        ll_yinjia.setOnClickListener(new OnClickListener() {//微信支付
//            @Override
//            public void onClick(View arg0) {
//                mPicChooseDialog.dismiss();
//                callBack.yinjia();
//            }
//        });
//
//        ll_tuniu.setOnClickListener(new OnClickListener() {//支付宝支付
//            @Override
//            public void onClick(View arg0) {
//                mPicChooseDialog.dismiss();
//                callBack.tuniu();
//            }
//        });
        return mPicChooseDialog;
    }

    /**
     * 转换群主提示
     *
     * @param context
     * @return
     */
    public static Dialog showQunTransferDialog(final Context context, final String content, final
    QunOwnerTransferDialogCallBack callbak) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_qun_transfer, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.loading_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView tv_cancle = (TextView) v.findViewById(R.id.tv_cancle);
        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_confirm.setTextColor(context.getResources().getColor(R.color.yellow2));
                mProgressDialog.dismiss();
                callbak.transfer();
            }
        });
        return mProgressDialog;
    }

    /**
     * 删除群成员
     *
     * @param context
     * @param callbak
     * @return
     */
    public static Dialog showGroupMemDelDialog(final Context context, String content, final GroupDelMemDialogCallback
            callbak) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_qun_dissolution, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.loading_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView tv_cancle = (TextView) v.findViewById(R.id.tv_cancle);
        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_confirm.setTextColor(context.getResources().getColor(R.color.yellow2));
                mProgressDialog.dismiss();
                callbak.del();
            }
        });
        return mProgressDialog;
    }

    /**
     * 退群
     *
     * @param context
     * @param callbak
     * @return
     */
    public static Dialog showexitGroupDialog(final Context context, String content, final ExitGroupDialogCallback
            callbak) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_qun_dissolution, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.loading_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView tv_cancle = (TextView) v.findViewById(R.id.tv_cancle);
        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_confirm.setTextColor(context.getResources().getColor(R.color.yellow2));
                mProgressDialog.dismiss();
                callbak.exit();
            }
        });
        return mProgressDialog;
    }

    /**
     * 解散群
     *
     * @param context
     * @param callbak
     * @return
     */
    public static Dialog showQunDissolutionDialog(final Context context, String content, final
    QunOwnerDissolutionDialogCallBack callbak) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_qun_dissolution, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.loading_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView tv_cancle = (TextView) v.findViewById(R.id.tv_cancle);
        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_confirm.setTextColor(context.getResources().getColor(R.color.yellow2));
                mProgressDialog.dismiss();
                callbak.dissolution();
            }
        });
        return mProgressDialog;
    }

    public static Dialog showRedPackageConfirmDialog(final Context context, String title, String content, final RedPackageCallback callback) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_redpackage, null);
        if (null == mProgressDialog) {
            mProgressDialog = new Dialog(context, R.style.loading_dialog);
        }
        mProgressDialog.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mProgressDialog.getWindow().setAttributes(lp);
        mProgressDialog.setCancelable(false);
        TextView tv_cancle = (TextView) v.findViewById(R.id.tv_cancle);
        final TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tv_dialog_title = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_dialog_title.setText(title);
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_confirm.setTextColor(context.getResources().getColor(R.color.yellow2));
                mProgressDialog.dismiss();
                callback.send();
            }
        });
        return mProgressDialog;
    }


    //版本更新
    public static Dialog showVersionDialog(Context context, final VersionDialogCallBack callBack) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_version, null);
        mVersionDialog = new Dialog(context, R.style.loading_dialog);
        mVersionDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mVersionDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mVersionDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mVersionDialog.getWindow().setAttributes(lp);
        mVersionDialog.setCanceledOnTouchOutside(false);

        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
//		TextView contentTv = (TextView) view.findViewById(R.id.tv_content);
//		contentTv.setText(content);
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mVersionDialog.dismiss();
                callBack.update();
            }
        });

        return mVersionDialog;
    }

    static OnKeyListener keylistener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                ActivityHelper.getInstance().finishAllActivity();
                return true;
            } else {
                return false;
            }
        }
    };


    //签名dailog

    public interface SignatureDialogCallBack {
        public void ok();

        public void cancel();
    }

    public static Dialog showSignatureDialog(Context context, final SignatureDialogCallBack signatureDialogCallBack) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_signature, null);
        mSignatureDialog = new Dialog(context, R.style.loading_dialog);
        mSignatureDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mSignatureDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mSignatureDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mSignatureDialog.getWindow().setAttributes(lp);
        mSignatureDialog.setCanceledOnTouchOutside(true);

        Button okCancel = (Button) view.findViewById(R.id.btn_canncel);
        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
        final LinePathView mPathView = (LinePathView) view.findViewById(R.id.view);
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mPathView.getTouched()) {
                    try {
                        mPathView.save("/sdcard/signature.png", true, 10);
                        mSignatureDialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    signatureDialogCallBack.ok();
                } else {
                    signatureDialogCallBack.cancel();
                }
                /*try {
                    mPathView.save("/sdcard/signature.png", true, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                signatureDialogCallBack.ok();*/
            }
        });
        okCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //mSignatureDialog.dismiss();
                mPathView.clear();
                signatureDialogCallBack.cancel();

            }
        });

        return mSignatureDialog;
    }

    public static Dialog showGroupLuckyPackageDialog(Context context, String imgUrl, String redPackageSender,
                                                     String leaveMsg, String invalid, String snatch,
                                                     final PackageDialogCallBack dialogCallBack) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_red_package_dialog, null);
        mPackageDialog = new Dialog(context, R.style.loading_dialog);
        mPackageDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mPackageDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mPackageDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mPackageDialog.getWindow().setAttributes(lp);
        mPackageDialog.setCanceledOnTouchOutside(true);

        RelativeLayout rel_close = (RelativeLayout) view.findViewById(R.id.rel_close);
        ImageView openBtn = (ImageView) view.findViewById(R.id.img_open);
        TextView txt_look_detail = (TextView) view.findViewById(R.id.txt_look_detail);
        CircleImageView img_head_portrait = (CircleImageView) view.findViewById(R.id.img_head_portrait);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
        TextView txt_default_msg = (TextView) view.findViewById(R.id.txt_default_msg);
        TextView txt_leave_msg = (TextView) view.findViewById(R.id.txt_leave_msg);
        TextView txt_failure_msg = (TextView) view.findViewById(R.id.txt_failure_msg);
        //头像展示
        BitmapUtils.displayImage(context, imgUrl, R.drawable.iv_head, img_head_portrait);

        txt_name.setText(redPackageSender);
        //invalid  false 有效 true失效    snatch  true 已抢 false 未抢
        if (!ObjectUtils.isNull(invalid) && "false".equals(invalid)) {
            if (!ObjectUtils.isNull(snatch) && "false".equals(snatch)) {
                txt_default_msg.setVisibility(View.VISIBLE);
                txt_leave_msg.setVisibility(View.VISIBLE);
                openBtn.setVisibility(View.VISIBLE);
                txt_failure_msg.setVisibility(View.GONE);
                txt_leave_msg.setText(leaveMsg);
            } else {
                txt_default_msg.setVisibility(View.GONE);
                txt_leave_msg.setVisibility(View.GONE);
                openBtn.setVisibility(View.GONE);
                txt_failure_msg.setVisibility(View.VISIBLE);
                txt_failure_msg.setText("手慢了，红包已抢完");
            }

        } else {
            txt_default_msg.setVisibility(View.GONE);
            txt_leave_msg.setVisibility(View.GONE);
            openBtn.setVisibility(View.GONE);
            txt_failure_msg.setVisibility(View.VISIBLE);
            txt_failure_msg.setText("该红包已失效");
        }

        //contentTv.setText(content);
        openBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPackageDialog.dismiss();
                dialogCallBack.open();
            }
        });
        rel_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPackageDialog.dismiss();

            }
        });
        txt_look_detail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPackageDialog.dismiss();
                dialogCallBack.look();
            }
        });

        return mPackageDialog;
    }

    public static Dialog showSinglePackageDialog(Context context, String imgUrl, String redPackageSender,
                                                 String leaveMsg, String invalid, String snatch,
                                                 final SinglePackageDialogCallBack dialogCallBack) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_red_single_package_dialog, null);
        mSinglePackageDialog = new Dialog(context, R.style.loading_dialog);
        mSinglePackageDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        Window window = mSinglePackageDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mSinglePackageDialog.getWindow().getAttributes();
        lp.width = display.getWidth() - 100;
        mSinglePackageDialog.getWindow().setAttributes(lp);
        mSinglePackageDialog.setCanceledOnTouchOutside(true);

        RelativeLayout rel_close = (RelativeLayout) view.findViewById(R.id.rel_close);
        ImageView openBtn = (ImageView) view.findViewById(img_open);
        CircleImageView img_head_portrait = (CircleImageView) view.findViewById(R.id.img_head_portrait);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
        TextView txt_default_msg = (TextView) view.findViewById(R.id.txt_default_msg);
        TextView txt_leave_msg = (TextView) view.findViewById(R.id.txt_leave_msg);
        TextView txt_failure_msg = (TextView) view.findViewById(R.id.txt_failure_msg);

        BitmapUtils.displayImage(context, imgUrl, R.drawable.iv_head, img_head_portrait);

        txt_name.setText(redPackageSender);
        //invalid  false 有效 true失效    snatch  true 已抢 false 未抢
        if (!ObjectUtils.isNull(invalid) && "false".equals(invalid)) {

            if (!ObjectUtils.isNull(snatch) && "false".equals(snatch)) {
                txt_default_msg.setVisibility(View.VISIBLE);
                txt_leave_msg.setVisibility(View.VISIBLE);
                openBtn.setVisibility(View.VISIBLE);
                txt_failure_msg.setVisibility(View.GONE);
                txt_leave_msg.setText(leaveMsg);
            }

        } else {
            txt_default_msg.setVisibility(View.GONE);
            txt_leave_msg.setVisibility(View.GONE);
            openBtn.setVisibility(View.GONE);
            txt_failure_msg.setVisibility(View.VISIBLE);
            txt_failure_msg.setText("该红包已失效");
        }

        //contentTv.setText(content);
        openBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSinglePackageDialog.dismiss();
                dialogCallBack.open();
            }
        });
        rel_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mSinglePackageDialog.dismiss();

            }
        });


        return mSinglePackageDialog;
    }


    public static void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void closeActivityDialog() {
        if (null != mActivityDialog) {
            mActivityDialog.dismiss();
            mActivityDialog = null;
        }
    }

    public static void closeAuditStatusDialog() {
        if (null != mAuditStatusDialog) {
            mAuditStatusDialog.dismiss();
            mAuditStatusDialog = null;
        }
    }
}
