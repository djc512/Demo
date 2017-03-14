package huanxing_print.com.cn.printhome.view.dialog;


import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;


public class DialogUtils {

	public static Dialog mProgressDialog;
	public static Dialog mShareDialog;
	public static Dialog mTipsDialog;
	public static Dialog mAuditStatusDialog;
	public static Dialog mLocationDialog;
	public static Dialog mMyReturnDialog;
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

	public interface PicChooseDialogCallBack {
		public void camera();

		public void photo();

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
			}
		});
		wechatBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonUtils.isWeixinAvilible(context)) {
				mShareDialog.dismiss();
				shareDialogCallBack.wechat();
				} else {
					ToastUtil.doToast(context, "您还没有安装微信，请先安装微信客户端");
				}
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
		cameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPicChooseDialog.dismiss();
				callBack.camera();
			}
		});

		photoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPicChooseDialog.dismiss();
				callBack.photo();
			}
		});
		return mPicChooseDialog;
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
