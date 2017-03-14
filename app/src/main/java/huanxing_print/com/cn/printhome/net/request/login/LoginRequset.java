package huanxing_print.com.cn.printhome.net.request.login;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.Config;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.ballback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.login.LoginResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;
import huanxing_print.com.cn.printhome.util.MD5Util;

import android.content.Context;

public class LoginRequset extends BaseRequst {

	public static void login(Context context,String phone, String password,String lat,String lon,String cityName,
			String sessionId,final LoginCallback callback) {

		String url = HTTP_URL + HttpUrl.Login;
		//password = MD5Util.MD5(password);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("password", password);
		params.put("lat", lat);
		params.put("lon", lon);
		params.put("clientType", Config.CLIENT_TYPE);
		params.put("channelId",  Config.CHANNEL_ID);
		params.put("cityName", cityName);
		params.put("sessionId", "");

		HttpUtils.post(context,url, "", params, new HttpCallBack() {

			@Override
			public void success(String content) {
				LoginResolve loginResolve = new LoginResolve(content);
				loginResolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}
	


}
