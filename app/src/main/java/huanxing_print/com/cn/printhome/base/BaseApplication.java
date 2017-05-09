package huanxing_print.com.cn.printhome.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;

import com.dreamlive.cn.clog.CollectLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
	private String loginToken;//登录校验token，需要登录的接口需要校验该token
	private String phone, passWord;
	private String easemobId;//环信聊天用户id
	private String  uniqueId;//印家号
	private String memberId;//会员id
	private String nickName;
	private String comId;
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
	public static final String WX_APPID = "wx4c877768d9a9fc08";
	public static final String WX_APPSecret = "d7ba93d327cfdd1d02b8d5a4b43b1223";
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
		//initHuanxin();
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
