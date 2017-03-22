package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.request.login.LoginRequset;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView tv_login;
	private EditText login_phone, login_pass;
	private TextView forget_pass, login_register;
	private ImageView iv_head,passShowIv,passNormalIv;
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
		CommonUtils.initSystemBar(this);
		setContentView(R.layout.activity_login);
		initViews();
		//initData();
	}

	private void initData() {
		phone = baseApplication.getPhone();
		headImg = baseApplication.getHeadImg();
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
		iv_head = (ImageView) findViewById(R.id.iv_head);
		passShowIv = (ImageView) findViewById(R.id.pass_show);
		passNormalIv = (ImageView) findViewById(R.id.pass_normal);
		passShowIv.setOnClickListener(this);
		passNormalIv.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		forget_pass.setOnClickListener(this);
		login_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:
			String name = login_phone.getText().toString().trim();
			password = login_pass.getText().toString().trim();
//			jumpActivity(MainActivity.class);
//			finishCurrentActivity();
			if (isUserNameAndPwdVali(name, password)) {
				DialogUtils.showProgressDialog(getSelfActivity(), "正在登录中").show();
				LoginRequset.login(getSelfActivity(), name, password, loginCallback);
			}
			break;
		case R.id.login_register://跳转注册界面
			jumpActivity(RegisterActivity.class);
			break;

		case R.id.forget_pass://跳转修改密码界面
			jumpActivity(ForgetPasswodActivity.class);
			break;
			case R.id.pass_normal:
				passShowIv.setVisibility(View.VISIBLE);
				passNormalIv.setVisibility(View.GONE);
				login_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				break;
			case R.id.pass_show:
				passShowIv.setVisibility(View.GONE);
				passNormalIv.setVisibility(View.VISIBLE);
				login_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
				break;
			default:
				break;
		}
	}
	//登录接口返回
	private LoginCallback loginCallback = new LoginCallback() {

		@Override
		public void success(LoginBean loginBean) {

			baseApplication.setHasLoginEvent(true);
			DialogUtils.closeProgressDialog();
			if (!ObjectUtils.isNull(loginBean)) {
				String loginToken =loginBean.getLoginToken();
				baseApplication.setLoginToken(loginToken);
				LoginBeanItem userInfo = loginBean.getMemberInfo();
				if (!ObjectUtils.isNull(userInfo)) {
					baseApplication.setPhone(userInfo.getMobileNumber());
					baseApplication.setSex(userInfo.getSex());
					baseApplication.setNickName(userInfo.getNickName());
					baseApplication.setHeadImg(userInfo.getFaceUrl());
					baseApplication.setPassWord(password);
					jumpActivity(MainActivity.class);
					finishCurrentActivity();
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
		} else if (psd.length()<6) {
			toast(getStringFromResource(R.string.phone_set_null));
			return false;
		}
		return true;
	}


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
