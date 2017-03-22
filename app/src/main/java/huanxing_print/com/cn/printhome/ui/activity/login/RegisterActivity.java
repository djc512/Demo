package huanxing_print.com.cn.printhome.ui.activity.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.request.register.RegisterRequst;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.time.ScheduledHandler;
import huanxing_print.com.cn.printhome.util.time.ScheduledTimer;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_title;
	private TextView  getCodeTv, registeTv;
	private EditText registerPhoneEt, registerCodeEt, registerPassEt;

	private String  password, phone, verCode;

	private ImageView passShowIv, passNormalIv;

	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	@Override
	protected boolean isNeedLocate() {
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 改变状态栏的颜色使其与APP风格一体化
		CommonUtils.initSystemBar(this);
		setContentView(R.layout.activity_register);
		initData();
		initViews();
	}

	private void initData() {
		setLocationCallBack(locationCallBack);
	}

	private void initViews() {
		getCodeTv = (TextView) findViewById(R.id.code_btn);
		registeTv = (TextView) findViewById(R.id.register_btn);

		registerPhoneEt = (EditText) findViewById(R.id.register_phonenum);
		registerCodeEt = (EditText) findViewById(R.id.register_code);
		registerPassEt = (EditText) findViewById(R.id.register_pass);
		findViewById(R.id.rl_title).setOnClickListener(this);
		getCodeTv.setOnClickListener(this);
		registeTv.setOnClickListener(this);

		passShowIv = (ImageView) findViewById(R.id.pass_show);
		passNormalIv = (ImageView) findViewById(R.id.pass_normal);
		passShowIv.setOnClickListener(this);
		passNormalIv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_title:
			finishCurrentActivity();
			break;
		case R.id.code_btn:// 获取验证码
			getVerCode();
			break;
		case R.id.register_btn:// 注册提交
			Logger.d("注册");
			register();
			break;
//		case R.id.reg_login:// 已经账号去登录
//			jumpActivity(LoginActivity.class);
//			break;
		case R.id.pass_normal:
			passShowIv.setVisibility(View.VISIBLE);
			passNormalIv.setVisibility(View.GONE);
			registerPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			break;
		case R.id.pass_show:
			passShowIv.setVisibility(View.GONE);
			passNormalIv.setVisibility(View.VISIBLE);
			registerPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
			break;
		default:
			break;
		}

	}

	/**
	 * 注册事件
	 */
	public void register() {
		phone = registerPhoneEt.getText().toString();
		password = registerPassEt.getText().toString();
		verCode = registerCodeEt.getText().toString();

		if (verify()) {
			DialogUtils.showProgressDialog(getSelfActivity(), "正在注册").show();
			RegisterRequst.register(getSelfActivity(),  password, phone, verCode,baseApplication.getWechatId(), registerCallback);
		}
	}

	/**
	 * 验证注册参数
	 */
	private boolean verify() {

		if (ObjectUtils.isNull(phone)) {
			ToastUtil.doToast(this, R.string.phone_no_null);
			return false;
		} else if (!CommonUtils.isPhone(phone)) {
			ToastUtil.doToast(this, R.string.phone_format_error);
			return false;
		} else if (ObjectUtils.isNull(verCode)) {
			ToastUtil.doToast(this, R.string.code_no_null);
			return false;
		} else if (ObjectUtils.isNull(password)) {
			ToastUtil.doToast(this, R.string.psd_no_null);
			return false;
		} else if (password.length()<6) {
			ToastUtil.doToast(this, R.string.psd_set);
			return false;
		} else if (CommonUtils.checkStrChese(password)) {
			ToastUtil.doToast(this, R.string.psd_set_no_chinese);
			return false;
		}
		return true;
		
		
		
	}

	/**
	 * 获取验证码
	 */
	private void getVerCode() {
		phone = registerPhoneEt.getText().toString();
		if (ObjectUtils.isNull(phone)) {
			toast("手机号不能为空");
			return;
		}
		if (!CommonUtils.isPhone(phone) || phone.length() < 11) {
			toast("手机号码格式有误");
			return;
		}
		registerPhoneEt.setEnabled(false);
		if (!ObjectUtils.isNull(phone)) {
			getCodeTv.setClickable(false);
			DialogUtils.showProgressDialog(getSelfActivity(), "正在获取验证码").show();
			RegisterRequst.getVerCode(getSelfActivity(),"1", phone, 1, getVerCodeCallback);
		}
	}

	private GetVerCodeCallback getVerCodeCallback = new GetVerCodeCallback() {

		@Override
		public void fail(String msg) {
			DialogUtils.closeProgressDialog();
			toast(msg);
			getCodeTv.setClickable(true);
			registerPhoneEt.setEnabled(true);
		}

		@Override
		public void connectFail() {
			DialogUtils.closeProgressDialog();
			toastConnectFail();
			getCodeTv.setClickable(true);
			registerPhoneEt.setEnabled(true);
		}

		@Override
		public void success(String msg) {
			DialogUtils.closeProgressDialog();
			toast("获取验证码成功");
			codeCountdown();
		}
	};

	private RegisterCallback registerCallback = new RegisterCallback() {

		public void fail(String msg) {
			DialogUtils.closeProgressDialog();
			toast(msg);
		}

		public void connectFail() {
			DialogUtils.closeProgressDialog();
			toastConnectFail();
		}

		public void success(LoginBean registerBean) {
			DialogUtils.closeProgressDialog();
			if (!ObjectUtils.isNull(registerBean)) {
			toast("注册成功");
			if (!ObjectUtils.isNull(registerBean)) {
				//String loginToken =registerBean.getLoginToken();
				//baseApplication.setLoginToken(loginToken);
				//LoginBeanItem userInfo = registerBean.getMemberInfo();
//				if (!ObjectUtils.isNull(userInfo)) {
//					baseApplication.setPhone(userInfo.getMobileNumber());
//					//jumpActivity(CompanyVerifyActivity.class);
//					//jumpActivity(MainActivity.class);
//			    	finishCurrentActivity();
//				}
				}
		}
	  }
	};
	

	
	
	/**
	 * 获取验证码成功倒计时
	 */
	private void codeCountdown() {
		ScheduledTimer scheduledTimer = new ScheduledTimer(new ScheduledHandler() {
			@Override
			public void post(int times) {
				getCodeTv.setText((60 - times) + "秒");
			}

			@Override
			public void end() {
				getCodeTv.setText("重新获取");
				getCodeTv.setClickable(true);
				registerPhoneEt.setEnabled(true);
			}
		}, 0, 1000, 60);
		scheduledTimer.start();
	}


}
