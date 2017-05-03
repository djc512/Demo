package huanxing_print.com.cn.printhome.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;


public class DialogUtils {

	public static Dialog mProgressDialog;
	public static Dialog mShareDialog;
	public static Dialog mTipsDialog;
	public static Dialog mAuditStatusDialog;
	public static Dialog mLocationDialog;
	public static Dialog mVersionDialog;
	public static Dialog mPicChooseDialog;
	public static Dialog mActivityDialog;

	public interface ShareDialogCallBack {

		public void wechat();

		public void wechatMoment();

		public void cancel();

	}

	public interface TipsDialogCallBack {
		public void ok();

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
		TextView contentTv = (TextView) view.findViewById(R.id.tv_content);
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

	public static Dialog showPayChooseDialog(Context context, final PayChooseDialogCallBack callBack) {
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
