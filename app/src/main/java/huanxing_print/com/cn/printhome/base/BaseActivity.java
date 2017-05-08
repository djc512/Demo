package huanxing_print.com.cn.printhome.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.login.HasLoginEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.logic.map.LocationCallBack;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

//import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends Activity {

	protected abstract BaseActivity getSelfActivity();
	protected BaseApplication baseApplication;
	protected LocationCallBack locationCallBack;

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	public void setLocationCallBack(LocationCallBack locationCallBack) {
		this.locationCallBack = locationCallBack;
	}

	/**
	 * 是否需要定位
	 * 
	 * @return
	 */
	protected boolean isNeedLocate() {
		return false;
	};

	/**
	 * 是否需要事件,基于eventbus(onEvent,onEventMainThread,onEventBackgroundThread,
	 * onEventAsync)
	 * 
	 * @return
	 */
	protected boolean isNeedEvent() {
		return false;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在当前界面注册一个订阅者
		//EventBus.getDefault().register(this);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//EventBus.getDefault().unregister(this);
		DialogUtils.closeProgressDialog();
		OkHttpUtils.getInstance().cancelTag(getSelfActivity());
	}

	private void init() {
		baseApplication = (BaseApplication) getApplication();
		ActivityHelper.getInstance().addActivity(getSelfActivity());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}

	/**
	 * 设置title
	 */
	protected void setTitle(String title) {
		LinearLayout backLl = (LinearLayout) getSelfActivity().findViewById(R.id.title_back);
		backLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishCurrentActivity();
				activityExitAnim();
			}
		});
		TextView titleTv = (TextView) getSelfActivity().findViewById(R.id.title_tv);
		titleTv.setText(title);
	}

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	protected boolean isLogin() {
		if (!ObjectUtils.isNull(baseApplication.getMemberId())) {
			return true;
		}
		return false;
	}

	public void jumpActivity(Class cls) {

		jumpActivity(null, cls);

	}

	public void jumpActivityNoAnim(Class cls) {

		jumpActivityNoAnim(null, cls);

	}

	/**
	 * 目标页需要登录
	 * 
	 * @param cls
	 * @param isBack(true:登录后跳转到目标页,false:进入主页)
	 */
	public void jumpActivity(Class cls, boolean isBack) {

		jumpActivity(null, cls, isBack);

	}

	public void jumpActivityNoAnim(Class cls, boolean isBack) {

		jumpActivityNoAnim(null, cls, isBack);

	}

	public void jumpActivity(Intent intent, Class cls) {

		if (null == intent) {
			startActivity(new Intent(getSelfActivity(), cls));
		} else {
			intent.setClass(getSelfActivity(), cls);
			startActivity(intent);
		}
		activityEnterAnim();

	}

	public void jumpActivityNoAnim(Intent intent, Class cls) {

		if (null == intent) {
			startActivity(new Intent(getSelfActivity(), cls));
		} else {
			intent.setClass(getSelfActivity(), cls);
			startActivity(intent);
		}

	}

	/**
	 * 目标页需要登录
	 * 
	 * @param intent
	 * @param cls
	 * @param isBack(true:登录后跳转到目标页,false:进入主页)
	 */
	public void jumpActivity(Intent intent, Class cls, boolean isBack) {

		Logger.d("userId:" + baseApplication.getMemberId());
		if (!ObjectUtils.isNull(baseApplication.getMemberId())) {
			jumpActivity(intent, cls);
			return;

		}

		if (null != intent) {
			intent.setClass(getSelfActivity(), LoginActivity.class);
			if (isBack) {
				intent.putExtra("isBack", true);
				intent.putExtra("className", cls.getName());
				Bundle bundle = intent.getBundleExtra("bundle");
				intent.putExtra("bundle", bundle);
			}
			startActivity(intent);
		} else {
			Intent newIntent = new Intent();
			newIntent.setClass(getSelfActivity(), LoginActivity.class);
			if (isBack) {
				newIntent.putExtra("isBack", true);
				newIntent.putExtra("className", cls.getName());
			}
			startActivity(newIntent);
		}
		activityEnterAnim();
	}

	public void jumpActivityNoAnim(Intent intent, Class cls, boolean isBack) {

		Logger.d("userId:" + baseApplication.getMemberId());
		if (!ObjectUtils.isNull(baseApplication.getMemberId())) {
			jumpActivity(intent, cls);
			return;

		}

		if (null != intent) {
			intent.setClass(getSelfActivity(), LoginActivity.class);
			if (isBack) {
				intent.putExtra("isBack", true);
				intent.putExtra("className", cls.getName());
				Bundle bundle = intent.getBundleExtra("bundle");
				intent.putExtra("bundle", bundle);
			}
			startActivity(intent);
		} else {
			Intent newIntent = new Intent();
			newIntent.setClass(getSelfActivity(), LoginActivity.class);
			if (isBack) {
				newIntent.putExtra("isBack", true);
				newIntent.putExtra("className", cls.getName());
			}
			startActivity(newIntent);
		}
	}

	public void jumpActivityForResult(Class cls, int reqCode) {

		jumpActivityForResult(null, cls, reqCode);

	}

	public void jumpActivityForResult(Intent intent, Class cls, int reqCode) {

		if (null == intent) {
			startActivityForResult(new Intent(getSelfActivity(), cls), reqCode);
		} else {
			intent.setClass(getSelfActivity(), cls);
			startActivityForResult(intent, reqCode);
		}
		activityEnterAnim();
	}

	public void activityEnterAnim() {
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public void activityExitAnim() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * finis指定activity
	 * 
	 * @param cls
	 */
	public void finishActivity(Class... cls) {
		if (!ObjectUtils.isNull(cls)) {
			for (Class c : cls) {
				ActivityHelper.getInstance().finishActivity(c);
				activityExitAnim();
			}
		}
	}

	/**
	 * finish当前activity
	 */
	public void finishCurrentActivity() {

		ActivityHelper.getInstance().finishCurrentActivity();

	}

	public void finishCurrentActivityOnAnim() {

		ActivityHelper.getInstance().finishCurrentActivity();
		activityExitAnim();

	}

	public int dip2px(float dipValue) {
		final float scale = getSelfActivity().getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public float px2dip(float pxValue) {
		final float scale = getSelfActivity().getResources().getDisplayMetrics().density;
		return pxValue / scale + 0.5f;
	}

	public void toast(String msg) {
		Toast.makeText(getSelfActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	public void toastConnectFail() {
		Toast.makeText(getSelfActivity(), "网络连接超时", Toast.LENGTH_SHORT).show();
	}

	public void longToast(String msg) {
		Toast.makeText(getSelfActivity(), msg, Toast.LENGTH_LONG).show();
	}

	public int getWindowWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public String getStringFromResource(int id) {

		return getResources().getString(id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishCurrentActivity();
			activityExitAnim();
			return false;

		}

		return super.onKeyDown(keyCode, event);
	}

	protected BDLocationListener locationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {

			baseApplication.setCity(ObjectUtils.formatString(location.getCity()));
			baseApplication.setLat(location.getLatitude());
			baseApplication.setLon(location.getLongitude());
			String address = ObjectUtils.formatString(location.getCity())
					+ ObjectUtils.formatString(location.getDistrict()) + ObjectUtils.formatString(location.getStreet())
					+ ObjectUtils.formatString(location.getStreetNumber());
			Logger.d("location:" + address);
			baseApplication.setAddress(address);
			if (null != locationCallBack) {
				locationCallBack.location(location);
			}

		}
	};


	/**
	 * 退出事件
	 *  //订阅事件FirstEvent
	 * @param hasLoginEvent
	 */
	public void onEventMainThread(HasLoginEvent hasLoginEvent) {
		    clearUserData();
			ActivityHelper.getInstance().finishAllActivity();
			activityExitAnim();
			jumpActivityNoAnim(null, LoginActivity.class);

	}

	protected void clearUserData() {
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "loginToken");
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "passWord");
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "sex");
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "headImg");
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "nickName");
		SharedPreferencesUtils.removeShareValue(getSelfActivity(), "comId");
		baseApplication.setPassWord("");
		baseApplication.setSex("");
		baseApplication.setNickName("");
		baseApplication.setHeadImg("");
		baseApplication.setLoginToken("");
		baseApplication.setComId("");
	}

	protected void initJPush(String alias) {
		JPushInterface.setAliasAndTags(getApplicationContext(), alias, null, new TagAliasCallback() {

			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				switch (code) {
				case 0:// 绑定成功
					Logger.d("initJPush:" + code + "-alias:-" + alias);
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, tags), 1000 * 2);
					break;
				case 6002:// 绑定失败
					if (!ObjectUtils.isNull(alias)) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 5);
					}
					break;

				default:

				}
			}
		});

	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:

				break;
			case MSG_SET_TAGS:
				initJPush(baseApplication.getMemberId());
				break;

			default:
			}
		}
	};
}
