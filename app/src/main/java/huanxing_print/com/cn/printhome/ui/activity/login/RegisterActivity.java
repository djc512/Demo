package huanxing_print.com.cn.printhome.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.request.register.RegisterRequst;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ThreadUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.time.ScheduledHandler;
import huanxing_print.com.cn.printhome.util.time.ScheduledTimer;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static android.R.attr.password;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_title;
	private TextView  getCodeTv, registeTv;
	private EditText registerPhoneEt, registerCodeEt ;

	private String  phone, verCode;

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
		getCodeTv.setOnClickListener(this);
		registeTv.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		default:
			break;
		}

	}

	/**
	 * 注册事件
	 */
	public void register() {
		phone = registerPhoneEt.getText().toString();
		verCode = registerCodeEt.getText().toString();

		if (verify()) {
			DialogUtils.showProgressDialog(getSelfActivity(), "正在注册").show();
			RegisterRequst.register(getSelfActivity(), phone, verCode, registerCallback);
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

		public void success(final LoginBean registerBean) {
			//成功了再去注册环信平台
			ThreadUtils.runOnSubThread(new Runnable() {
				@Override
				public void run() {
					try {
						EMClient.getInstance().createAccount(phone, verCode);
						//环信注册成功
						ThreadUtils.runOnMainThread(new Runnable() {
							@Override
							public void run() {
								DialogUtils.closeProgressDialog();
								toast("注册成功");
								if (!ObjectUtils.isNull(registerBean)) {
									String loginToken = registerBean.getLoginToken();
									baseApplication.setLoginToken(loginToken);
									LoginBeanItem userInfo = registerBean.getMemberInfo();
									if (!ObjectUtils.isNull(userInfo)) {
										baseApplication.setPhone(userInfo.getMobileNumber());
										baseApplication.setNickName(userInfo.getNickName());
										baseApplication.setHeadImg(userInfo.getFaceUrl());
										baseApplication.setEasemobId(userInfo.getEasemobId());
										baseApplication.setUniqueId(userInfo.getUniqueId());
										if (!ObjectUtils.isNull(userInfo.getWechatId())) {
											baseApplication.setWechatId(userInfo.getWechatId());
										}
										jumpActivity(MainActivity.class);
										finishCurrentActivity();
									}
								}
							}
						});
					} catch (final HyphenateException e1) {
						e1.printStackTrace();
						//将Bmob上注册的user给删除掉
						//user.delete();
						//环信注册失败了
						ThreadUtils.runOnMainThread(new Runnable() {
							@Override
							public void run() {
//								mRegistView.onRegist(username,pwd,false,e1.toString());
								DialogUtils.closeProgressDialog();
								toast("环信登录失败");
							}
						});
					}
				}
			});


			/*DialogUtils.closeProgressDialog();
			    toast("注册成功");
				if (!ObjectUtils.isNull(registerBean)) {
					String loginToken = registerBean.getLoginToken();
					baseApplication.setLoginToken(loginToken);
					LoginBeanItem userInfo = registerBean.getMemberInfo();
					if (!ObjectUtils.isNull(userInfo)) {
						baseApplication.setPhone(userInfo.getMobileNumber());
						baseApplication.setNickName(userInfo.getNickName());
						baseApplication.setHeadImg(userInfo.getFaceUrl());
						baseApplication.setEasemobId(userInfo.getEasemobId());
						baseApplication.setUniqueId(userInfo.getUniqueId());
						if (!ObjectUtils.isNull(userInfo.getWechatId())) {
							baseApplication.setWechatId(userInfo.getWechatId());
						}
						jumpActivity(MainActivity.class);
						finishCurrentActivity();
					}
				}*/

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
