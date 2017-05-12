package huanxing_print.com.cn.printhome.base;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.dreamlive.cn.clog.CollectLog;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.ui.adapter.MessageListenerAdapter;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ThreadUtils;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import okhttp3.OkHttpClient;

//import com.dreamlive.cn.clog.CollectLog;

public class BaseApplication extends Application {
	private String loginToken;//登录校验token，需要登录的接口需要校验该token
	private String phone, passWord;
	private String easemobId;//环信聊天用户id
	private String  uniqueId;//印家号
	private String memberId;//会员id
	private String nickName;
	private String weixinName;
	private String comId;
	private SoundPool mSoundPool;
	private int mDuanSound;
	private int mYuluSound;
	private List<BaseActivity> mBaseActivityList = new ArrayList<>();
	private String sex;
	private String headImg;
	private String wechatId;
	private String address, city;
	private double lat, lon;
	public static int num = 9;
	//判断是否第一次进入
	private boolean isFirstEnter=true;
	//判断App是否为最新版本
	private boolean isNewApp=false;
	//APK下载地址
	private String ApkUrl;
	//判断是否登录
	private boolean hasLoginEvent=false;
   //微信第三方登录
	public static final String WX_APPID = "wxb54a2ee8a63993f9";
	public static final String WX_APPSecret = "c8c5ed7d1e388e54cb5a1b4c1af35663";
	private IWXAPI api;

	private static BaseApplication mInstance;

	public synchronized static BaseApplication getInstance() {
		return mInstance;
	}

	public boolean isFirstEnter() {
		isFirstEnter = SharedPreferencesUtils.getShareBoolean(this, "isFirstEnter",true);
		return isFirstEnter;
	}

	public void setFirstEnter(boolean firstEnter) {
		SharedPreferencesUtils.putShareValue(this, "isFirstEnter", firstEnter);
		isFirstEnter = firstEnter;
	}

	public boolean isNewApp() {
		isNewApp = SharedPreferencesUtils.getShareBoolean(this, "isNewApp",true);
		return isNewApp;
	}

	public void setNewApp(boolean newApp) {
		SharedPreferencesUtils.putShareValue(this, "isNewApp", newApp);
		isNewApp = newApp;
	}

	public String getApkUrl() {
		if (ObjectUtils.isNull(ApkUrl)) {
			ApkUrl = SharedPreferencesUtils.getShareString(this, "ApkUrl");
		}
		return ApkUrl;
	}

	public void setApkUrl(String apkUrl) {
		SharedPreferencesUtils.putShareValue(this, "ApkUrl", apkUrl);
		ApkUrl = apkUrl;
	}

	public boolean isHasLoginEvent() {
		hasLoginEvent = SharedPreferencesUtils.getShareBoolean(this, "hasLoginEvent",false);
		return hasLoginEvent;
	}

	public void setHasLoginEvent(boolean hasLoginEvent) {
		SharedPreferencesUtils.putShareValue(this, "hasLoginEvent", hasLoginEvent);
		this.hasLoginEvent = hasLoginEvent;
	}

	public String getLoginToken() {

		if (ObjectUtils.isNull(loginToken)) {
			loginToken = SharedPreferencesUtils.getShareString(this, "loginToken");
		}

		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		SharedPreferencesUtils.putShareValue(this, "loginToken", loginToken);
		this.loginToken = loginToken;
	}
	public String getPhone() {
		if (ObjectUtils.isNull(phone)) {
			phone = SharedPreferencesUtils.getShareString(this, "phone");
		}
		return phone;
	}

	public void setPhone(String phone) {
		SharedPreferencesUtils.putShareValue(this, "phone", phone);
		this.phone = phone;
	}

	public String getPassWord() {
		if (ObjectUtils.isNull(passWord)) {
			passWord = SharedPreferencesUtils.getShareString(this, "passWord");
		}
		return passWord;
	}

	public void setPassWord(String passWord) {
		SharedPreferencesUtils.putShareValue(this, "passWord", passWord);
		this.passWord = passWord;
	}

	public String getEasemobId() {
		if (ObjectUtils.isNull(easemobId)) {
			easemobId = SharedPreferencesUtils.getShareString(this, "easemobId");
		}
		return easemobId;
	}

	public void setEasemobId(String easemobId) {
		SharedPreferencesUtils.putShareValue(this, "easemobId", easemobId);
		this.easemobId = easemobId;
	}

	public String getUniqueId() {
		if (ObjectUtils.isNull(uniqueId)) {
			uniqueId = SharedPreferencesUtils.getShareString(this, "uniqueId");
		}
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		SharedPreferencesUtils.putShareValue(this, "uniqueId", uniqueId);
		this.uniqueId = uniqueId;
	}

	public String getMemberId() {
		if (ObjectUtils.isNull(memberId)) {
			memberId = SharedPreferencesUtils.getShareString(this, "memberId");
		}
		return memberId;
	}

	public void setMemberId(String memberId) {
		SharedPreferencesUtils.putShareValue(this, "memberId", memberId);
		this.memberId = memberId;
	}

	public String getWeixinName() {
		if (ObjectUtils.isNull(weixinName)) {
			weixinName = SharedPreferencesUtils.getShareString(this, "weixinName");
		}
		return weixinName;
	}

	public void setWeixinName(String weixinName) {
		SharedPreferencesUtils.putShareValue(this, "weixinName", weixinName);
		this.weixinName = weixinName;
	}

	public String getNickName() {
		if (ObjectUtils.isNull(nickName)) {
			nickName = SharedPreferencesUtils.getShareString(this, "nickName");
		}
		return nickName;
	}

	public void setNickName(String nickName) {
		SharedPreferencesUtils.putShareValue(this, "nickName", nickName);
		this.nickName = nickName;
	}

	public String getComId() {
		if (ObjectUtils.isNull(comId)) {
			comId = SharedPreferencesUtils.getShareString(this, "comId");
		}
		return comId;
	}

	public void setComId(String comId) {
		SharedPreferencesUtils.putShareValue(this, "comId", comId);
		this.comId = comId;
	}

	public String getSex() {
		if (ObjectUtils.isNull(sex)) {
			sex = SharedPreferencesUtils.getShareString(this, "sex");
		}
		return sex;
	}

	public void setSex(String sex) {
		SharedPreferencesUtils.putShareValue(this, "sex", sex);
		this.sex = sex;
	}

	public String getHeadImg() {
		if (ObjectUtils.isNull(headImg)) {
			headImg = SharedPreferencesUtils.getShareString(this, "headImg");
		}
		return headImg;
	}

	public void setHeadImg(String headImg) {
		SharedPreferencesUtils.putShareValue(this, "headImg", headImg);
		this.headImg = headImg;
	}

	public String getWechatId() {
		if (ObjectUtils.isNull(wechatId)) {
			wechatId = SharedPreferencesUtils.getShareString(this, "wechatId");
		}
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		SharedPreferencesUtils.putShareValue(this, "wechatId", wechatId);
		this.wechatId = wechatId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		CollectLog clog = CollectLog.getInstance();
		clog.init(this);
		api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
		api.registerApp(WX_APPID);
		initJPush();
		initHttpConnection();
		ZXingLibrary.initDisplayOpinion(this);
		initHuanxin();



	}

	private void initHuanxin() {
			EMOptions options = new EMOptions();
	// 默认添加好友时，是不需要验证的，改成需要验证
			options.setAcceptInvitationAlways(false);

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

		if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
			//Log.e(TAG, "enter the service process!");

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

	//初始化
			EMClient.getInstance().init(this, options);
	//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
			EMClient.getInstance().setDebugMode(true);
	//添加消息的监听
		initMessageListener();
		//监听连接状态的改变
		initConnectionListener();

	}


	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	private void initMessageListener() {
		EMClient.getInstance().chatManager().addMessageListener(new MessageListenerAdapter() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onMessageReceived(List<EMMessage> list) {
				super.onMessageReceived(list);
				if (list != null && list.size() > 0) {
					/**
					 * 1. 判断当前应用是否在后台运行
					 * 2. 如果是在后台运行，则发出通知栏
					 * 3. 如果是在后台发出长声音
					 * 4. 如果在前台发出短声音
					 */
					Log.i("CMCC","收到消息了666666666666666666666666666666");

					EventBus.getDefault().post(list.get(0));
					/*if (isRuninBackground()) {
						sendNotification(list.get(0));
						//发出长声音
						//参数2/3：左右喇叭声音的大小
						mSoundPool.play(mYuluSound,1,1,0,0,1);
					} else {
						//发出短声音
						mSoundPool.play(mDuanSound,1,1,0,0,1);
					}*/

				}
			}
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void sendNotification(EMMessage message) {
		EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//延时意图
		/**
		 * 参数2：请求码 大于1
		 */
		Intent mainIntent = new Intent(this,MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Intent chatIntent = new Intent(this, ChatActivity.class);
		chatIntent.putExtra("username",message.getFrom());

		Intent[] intents = {mainIntent,chatIntent};
		PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT) ;
		Notification notification = new Notification.Builder(this)
				.setAutoCancel(true) //当点击后自动删除
				.setSmallIcon(R.mipmap.message) //必须设置
				.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.default_avatar))
				.setContentTitle("您有一条新消息")
				.setContentText(messageBody.getMessage())
				.setContentInfo(message.getFrom())
				.setContentIntent(pendingIntent)
				//.setPriority(Notification.PRIORITY_MAX)
				.build();
		notificationManager.notify(1,notification);
	}

	private boolean isRuninBackground() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
		ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
		if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
			return false;
		} else {
			return true;
		}
	}


	private void initConnectionListener() {
		EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
			@Override
			public void onConnected() {
			}

			@Override
			public void onDisconnected(int i) {
				if (i== EMError.USER_LOGIN_ANOTHER_DEVICE){
					// 显示帐号在其他设备登录
					/**
					 *  将当前任务栈中所有的Activity给清空掉
					 *  重新打开登录界面
					 */
					for (BaseActivity baseActivity : mBaseActivityList) {
						baseActivity.finish();
					}

					Intent intent = new Intent(BaseApplication.this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					ThreadUtils.runOnMainThread(new Runnable() {
						@Override
						public void run() {
							ToastUtils.showToast(BaseApplication.this,"您已在其他设备上登录了，请重新登录。");
						}
					});

				}
			}
		});
	}


	private void initHttpConnection() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				// .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(ConFig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
				.readTimeout(ConFig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS).build();

		OkHttpUtils.initClient(okHttpClient);

	}
	
	private void initJPush() {
        JPushInterface.setDebugMode(false);     // 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext());             // 初始化 JPush
	}


}
