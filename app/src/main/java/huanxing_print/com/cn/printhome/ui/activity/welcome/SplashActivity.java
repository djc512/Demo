package huanxing_print.com.cn.printhome.ui.activity.welcome;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
	private static final int delayMillis = 1000;

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
		if (isFirstEnter()) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					jumpActivity(GuideActivity.class);// 引导页
					finishCurrentActivity();
				}
			}, delayMillis);
		} else {
			//autoLogin();
			jumpActivity(LoginActivity.class);
			finishCurrentActivity();
		}
	}

	private boolean isFirstEnter() {
		String isFirst = SharedPreferencesUtils.getShareString(getSelfActivity(), SHAREDPREFERENCES_NAME,
				KEY_GUIDE_ACTIVITY);
		if ("false".equals(isFirst)) {
			return false;
		} else {
			return true;
		}

	}

	private void autoLogin() {
		String phone = baseApplication.getPhone();
		String password = baseApplication.getPassWord();
		String lat = baseApplication.getLat() + "";
		String lon = baseApplication.getLon() + "";
		String cityName = baseApplication.getCity();
		String sessionId = baseApplication.getSessionId();

		if (ObjectUtils.isAllNotNull(phone, password)) {
			LoginRequset.login(getSelfActivity(), phone, password, lat, lon, cityName, sessionId, loginCallback);
		} else {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					jumpActivity(LoginActivity.class);
					finishCurrentActivity();
				}
			}, delayMillis);

		}
	}

	private LoginCallback loginCallback = new LoginCallback() {

		@Override
		public void success(LoginBean loginBean) {
			baseApplication.setHasLoginEvent(false);
			DialogUtils.closeProgressDialog();
			if (!ObjectUtils.isNull(loginBean)) {
				LoginBeanItem userInfo = loginBean.getUserInfo();
				if (!ObjectUtils.isNull(loginBean)) {

					baseApplication.setUserId(userInfo.getUserId());
					baseApplication.setSessionId(userInfo.getSessionId());
					baseApplication.setUserName(userInfo.getUserName());
					baseApplication.setPhone(userInfo.getPhone());
					baseApplication.setHeadImg(userInfo.getHeadImg());
					String approvalStatus = userInfo.getApprovalStatus();
					jumpActivity(MainActivity.class);
					finishCurrentActivity();
					
//					if ("1".equals(approvalStatus)) {
//						Intent intent = new Intent();
//						intent.putExtra("name", baseApplication.getUserName());
//						intent.putExtra("approvalStatus", "1");
//						jumpActivity(intent, UserVerifyActivity.class);
//						finishCurrentActivity();
//					} else if ("3".equals(approvalStatus)) {
//						jumpActivity(MainActivity.class);
//						finishCurrentActivity();
//					} else if ("2".equals(approvalStatus) || "4".equals(approvalStatus)) {
//						Intent intent = new Intent();
//						intent.putExtra("approvalStatus", approvalStatus);
//						jumpActivity(intent, UserVerifyActivity.class);
//					}
				}

			}

		}

		@Override
		public void fail(String msg) {
			jumpActivity(LoginActivity.class);
			finishCurrentActivity();

		}

		@Override
		public void connectFail() {
			jumpActivity(LoginActivity.class);
			finishCurrentActivity();
		}

	};
}
