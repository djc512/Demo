package huanxing_print.com.cn.printhome.ui.activity.login;

import com.baidu.location.BDLocation;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.logic.map.LocationCallBack;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.imageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView tv_login;
	private EditText login_phone, login_pass;
	private TextView forget_pass, login_register;
	private CircleImageView iv_head;
	private String phone, headImg;
	private boolean isLoginOutDialogShow;// 是否显示登出的Dialog

	public static boolean isForeground = false;
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	private long exitTime = 0;
	private String password;

	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	@Override
	protected boolean   isNeedLocate() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 改变状态栏的颜色使其与APP风格一体化
		//CommonUtils.initSystemBar(this);
		setContentView(R.layout.activity_login);
		initViews();
		//initData();
	}

	private void initData() {
		phone = baseApplication.getPhone();
		headImg = baseApplication.getHeadImg();
		BitmapUtils.displayImage(getSelfActivity(), headImg, iv_head);
		if (!ObjectUtils.isNull(phone)) {
			login_phone.setText(phone);
		}

	}

	private void initViews() {
		login_phone = (EditText) findViewById(R.id.login_user);
		login_pass = (EditText) findViewById(R.id.login_pass);
		forget_pass = (TextView) findViewById(R.id.forget_pass);
		login_register = (TextView) findViewById(R.id.login_register);
		tv_login = (TextView) findViewById(R.id.tv_login);
		iv_head = (CircleImageView) findViewById(R.id.iv_head);

		tv_login.setOnClickListener(this);
		forget_pass.setOnClickListener(this);
		login_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:
			// jumpActivity(IncomeDetailActivity.class);
			String name = login_phone.getText().toString().trim();
			String pwd = login_pass.getText().toString().trim();
			jumpActivity(MainActivity.class);
			finishCurrentActivity();
//			if (isUserNameAndPwdVali(name, pwd)) {
//				String md5Pwd = MD5Util.MD5(pwd);
//				password = md5Pwd;
//				String lat = baseApplication.getLat()+"";
//				String lon = baseApplication.getLon()+"";
//				String cityName = baseApplication.getCity();
//				String sessionId = baseApplication.getSessionId();
//				DialogUtils.showProgressDialog(getSelfActivity(), "正在登录中").show();
//				LoginRequset.login(getSelfActivity(), name, md5Pwd, lat, lon, cityName, sessionId, loginCallback);
//			}
			break;
		case R.id.login_register://跳转注册界面
			jumpActivity(RegisterActivity.class);
			break;

//		case R.id.forget_pass://跳转修改密码界面
//			jumpActivity(ModifyPasswodActivity.class);
//			break;
		
		}
	}
    //登录接口返回
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
					baseApplication.setPassWord(password);

//					if ("1".equals(approvalStatus)) {
//						Intent intent = new Intent();
//						intent.putExtra("name", baseApplication.getUserName());
//						intent.putExtra("approvalStatus", "1");
//						jumpActivity(intent, UserVerifyActivity.class);
//						finishCurrentActivity();
//					} else if ("3".equals(approvalStatus)) {
//						startService(new Intent(getSelfActivity(), LocationServer.class));
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
			DialogUtils.closeProgressDialog();
			toast(msg);

		}

		@Override
		public void connectFail() {
			Logger.d("connectFail:");
			DialogUtils.closeProgressDialog();
			toastConnectFail();
		}

	};

	/*
	 * 判断用户名和密码是否有效
	 */
	private boolean isUserNameAndPwdVali(String name, String psd) {
		if (ObjectUtils.isNull(name)) {
			toast(getStringFromResource(R.string.phone_no_null));
			return false;
		} else if (!CommonUtils.isPhone(name)) {
			toast(getStringFromResource(R.string.phone_format_error));
			return false;
		} else if (ObjectUtils.isNull(psd)) {
			toast(getStringFromResource(R.string.psd_no_null));
			return false;
		} else if (psd.length()<5) {
			toast(getStringFromResource(R.string.phone_set_null));
			return false;
		}
		return true;
	}

	private LocationCallBack locationCallBack = new LocationCallBack() {
		@Override
		public void location(BDLocation location) {
			baseApplication.setLat(location.getLatitude());
			baseApplication.setLon(location.getLongitude());
			baseApplication.setCity(ObjectUtils.formatString(location.getCity()));
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				ActivityHelper.getInstance().finishAllActivity();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
