package huanxing_print.com.cn.printhome.base;

import java.util.concurrent.TimeUnit;

import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import cn.jpush.android.api.JPushInterface;

import com.zhy.http.okhttp.OkHttpUtils;
import android.app.Application;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
	private String loginToken;//登录校验token，需要登录的接口需要校验该token
	private String phone, passWord;
	private String userId;
	private String nickName;
	private String comId;
	private String sex;
	private String headImg;
	private String address, city;
	private double lat, lon;
	
	public static int num = 9;

	private boolean hasLoginEvent=false;

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

	public String getUserId() {
		if (ObjectUtils.isNull(userId)) {
			userId = SharedPreferencesUtils.getShareString(this, "userId");
		}
		return userId;
	}

	public void setUserId(String userId) {
		SharedPreferencesUtils.putShareValue(this, "userId", userId);
		this.userId = userId;
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
		initJPush();
		initHttpConnection();
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
