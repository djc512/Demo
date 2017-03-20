package huanxing_print.com.cn.printhome.base;

import android.app.Application;

import com.dreamlive.cn.clog.CollectLog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import huanxing_print.com.cn.printhome.constant.Config;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    private String sessionId;
    private String phone, passWord;
    private String userId;
    private String userName;
    private String headImg;
    private String address, city;
    private double lat, lon;
    private static BaseApplication mInstance;

    private boolean hasLoginEvent;

    public synchronized static BaseApplication getInstance() {
        return mInstance;
    }

    public boolean isHasLoginEvent() {
        return hasLoginEvent;
    }

    public void setHasLoginEvent(boolean hasLoginEvent) {
        this.hasLoginEvent = hasLoginEvent;
    }

    public String getSessionId() {

        if (ObjectUtils.isNull(sessionId)) {
            sessionId = SharedPreferencesUtils.getShareString(this, "sessionId");
        }

        return sessionId;
    }

    public void setSessionId(String sessionId) {
        SharedPreferencesUtils.putShareValue(this, "sessionId", sessionId);
        this.sessionId = sessionId;
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


    public String getUserName() {
        if (ObjectUtils.isNull(userName)) {
            userName = SharedPreferencesUtils.getShareString(this, "userName");
        }
        return userName;
    }

    public void setUserName(String userName) {
        SharedPreferencesUtils.putShareValue(this, "userName", userName);
        this.userName = userName;
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
        CollectLog clog = CollectLog.getInstance();
        clog.init(this);
        mInstance = this;
        initHttpConnection();
    }

    private void initHttpConnection() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(Config.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Config.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS).build();

        OkHttpUtils.initClient(okHttpClient);

    }


}
