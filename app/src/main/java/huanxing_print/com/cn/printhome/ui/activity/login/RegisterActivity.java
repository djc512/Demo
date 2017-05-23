package huanxing_print.com.cn.printhome.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.listener.EmsCallBackListener;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.model.login.LoginBeanItem;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.request.register.RegisterRequst;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.time.ScheduledHandler;
import huanxing_print.com.cn.printhome.util.time.ScheduledTimer;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static android.R.attr.password;
import static huanxing_print.com.cn.printhome.R.id.tv_login;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_title;
	private TextView  getCodeTv, registeTv;
	private EditText registerPhoneEt, registerCodeEt ;
	private String  phone, verCode;
	private ImageView iv_phone_detele,iv_code_detele;
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
		//setLocationCallBack(locationCallBack);
	}

	private void initViews() {
		getCodeTv = (TextView) findViewById(R.id.code_btn);
		registeTv = (TextView) findViewById(R.id.register_btn);
		iv_phone_detele = (ImageView) findViewById(R.id.iv_phone_detele);
		iv_code_detele = (ImageView) findViewById(R.id.iv_code_detele);
		registerPhoneEt = (EditText) findViewById(R.id.register_phonenum);
		registerCodeEt = (EditText) findViewById(R.id.register_code);
		getCodeTv.setOnClickListener(this);
		iv_phone_detele.setOnClickListener(this);
		iv_code_detele.setOnClickListener(this);
		registeTv.setOnClickListener(this);
		findViewById(R.id.tv_login).setOnClickListener(this);



		registerPhoneEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 获得焦点
				if (hasFocus) {
					iv_code_detele.setVisibility(View.GONE);
					phone = registerPhoneEt.getText().toString().trim();
					if(!ObjectUtils.isNull(phone)){
						iv_phone_detele.setVisibility(View.VISIBLE);
					}else{
						iv_phone_detele.setVisibility(View.GONE);
					}
				} else {

					// 失去焦点

				}

			}


		});
		registerPhoneEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (start > 0) {
					iv_phone_detele.setVisibility(View.VISIBLE);
					registeTv.setBackgroundResource(R.drawable.broder_yellow_full);
					registeTv.setTextColor(getResources().getColor(R.color.black2));
				} else {
					iv_phone_detele.setVisibility(View.GONE);
					registeTv.setBackgroundResource(R.drawable.broder_yellow4_full);
					registeTv.setTextColor(getResources().getColor(R.color.white));
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		registerCodeEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 获得焦点
				if (hasFocus) {
					iv_phone_detele.setVisibility(View.GONE);
					verCode = registerCodeEt.getText().toString().trim();
					if(!ObjectUtils.isNull(verCode)){
						iv_code_detele.setVisibility(View.VISIBLE);
					}else{
						iv_code_detele.setVisibility(View.GONE);
					}
				} else {

					// 失去焦点

				}

			}


		});
		registerCodeEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (start>0){
					iv_code_detele.setVisibility(View.VISIBLE);
				}else{
					iv_code_detele.setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
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
		case tv_login:// 登录
			jumpActivity(LoginActivity.class);
			break;
		case R.id.iv_phone_detele://
			registerPhoneEt.setText("");
			break;
		case R.id.iv_code_detele://
			registerCodeEt.setText("");
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
			//判断环信是否登录成功
			EMClient.getInstance().login(registerBean.getMemberInfo().getMemberId(), registerBean.getMemberInfo().getMemberId(), new EmsCallBackListener() {
				@Override
				public void onMainSuccess() {
					EMClient.getInstance().chatManager().loadAllConversations();
					EMClient.getInstance().groupManager().loadAllGroups();

					baseApplication.setHasLoginEvent(true);
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
							baseApplication.setMemberId(userInfo.getMemberId());
							baseApplication.setUniqueId(userInfo.getUniqueId());
							if (!ObjectUtils.isNull(userInfo.getWechatId())) {
								baseApplication.setWechatId(userInfo.getWechatId());
							}
							if (!ObjectUtils.isNull(userInfo.getWechatName())) {
								baseApplication.setWechatName(userInfo.getWechatName());
							}
							jumpActivity(MainActivity.class);
							finishCurrentActivity();
						}
					}

				}

				@Override
				public void onMainError(int i, String s) {
					DialogUtils.closeProgressDialog();
					toast("环信登录失败");

				}
			});
			Log.d("CMCC", "登陆人环信号:" + registerBean.getMemberInfo().getEasemobId());

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
