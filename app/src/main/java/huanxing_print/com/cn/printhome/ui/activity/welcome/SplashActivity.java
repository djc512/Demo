package huanxing_print.com.cn.printhome.ui.activity.welcome;

import android.os.Bundle;
import android.os.Handler;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.AppUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

public class SplashActivity extends BaseActivity {

	private static final int delayMillis = 1000;
	private boolean isFirst;
	private String version;

	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	protected boolean isNeedLocate() {
		return true;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		version = AppUtils.getVersionName(getSelfActivity());

//		 if (isFirstEnter()) {
//		 new Handler().postDelayed(new Runnable() {
//		 @Override
//		 public void run() {
//		 jumpActivity(GuideActivity.class);// 引导页
//		 finishCurrentActivity();
//		 }
//		 }, delayMillis);
//		 } else {
//			 new Handler().postDelayed(new Runnable() {
//				 @Override
//				 public void run() {
//
//					 autoLogin();
//				 }
//			 }, delayMillis);
//
//		 }
			 new Handler().postDelayed(new Runnable() {
				 @Override
				 public void run() {

					 autoLogin();
				 }
			 }, delayMillis);


		// 判断是否有网络
//		if (CommonUtils.isNetWorkConnected(this)) {
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// autoLogin();
//					VersionRequset.updateVersion(getSelfActivity(), version,callback);
//				}
//			}, delayMillis);
//		}else{
//			toast("没有可用的网络连接，请打开蜂窝数据或者wifi");
//		}
	}
//判断是否第一次打开
	 private boolean isFirstEnter() {
	 boolean isFirst = baseApplication.isFirstEnter();
	 if (isFirst) {
	 return true;
	 } else {
	 return false;
	 }

	 }

	private void autoLogin() {
		String uniqueId = baseApplication.getUniqueId();//印家号
		String password = baseApplication.getPassWord();
		if (!ObjectUtils.isNull(uniqueId)) {
			jumpActivity(MainActivity.class);
		} else {
			jumpActivityNoAnim(LoginActivity.class);
//			 new Handler().postDelayed(new Runnable() {
//			 @Override
//			 public void run() {
//			 jumpActivity(LoginActivity.class);
//			 }
//			 }, delayMillis);
		}
		finishCurrentActivity();
	}

//	private VersionCallback callback = new VersionCallback() {
//
//		@Override
//		public void success(GetVersionBean bean) {
//			baseApplication.setHasLoginEvent(false);
//			if (!ObjectUtils.isNull(bean)) {
//				//String deployTime = bean.getDeployTime(); // 发布日期
//				final String downloadUrl = bean.getDownloadUrl(); // 下载地址
//				String isForceUpdate = bean.getIsForceUpdate(); // 是否要强制更新
//				String isNew = bean.getIsNew(); // 是否是最新版本0 否 1 是最新版本
//				String versionCode = bean.getVersionCode(); // 版本号
//				//String versionDetail = bean.getVersionDetail(); // 版本更新细节
//				baseApplication.setApkUrl(downloadUrl);
//				if ("0".equals(isNew)) {
//					baseApplication.setNewApp(false);
//					if(!ObjectUtils.isNull(isForceUpdate)&&"1".equals(isForceUpdate)){
//						DialogUtils.showVersionDialog(getSelfActivity(),new DialogUtils.VersionDialogCallBack() {
//
//							public void update() {
//								if (!ObjectUtils.isNull(downloadUrl)) {
//									Uri uri = Uri.parse(downloadUrl);
//									Intent it = new Intent(Intent.ACTION_VIEW, uri);
//									startActivity(it);
//								}
//							}
//						}).show();
//
//					}
//				}else{
//					baseApplication.setNewApp(true);
//				}
//			}
//
//		}
//
//		@Override
//		public void fail(String msg) {
//			ToastUtil.doToast(getSelfActivity(),"服务器连接失败，请检查网络！");
//		}
//
//		@Override
//		public void connectFail() {
//			ToastUtil.doToast(getSelfActivity(),"网络连接失败，请检查网络！");
//			//autoLogin();
//		}
//
//	};
}
