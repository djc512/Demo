package huanxing_print.com.cn.printhome.net.request.login;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.callback.login.WeiXinCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.login.LoginResolve;
import huanxing_print.com.cn.printhome.net.resolve.login.WeiXinResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

public class LoginRequset extends BaseRequst {

	public static void login(Context context, String phone, String validCode,
							 final LoginCallback callback) {

		String url = HTTP_URL + HttpUrl.login;
		// password = MD5Util.MD5(password);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNumber", phone);
		params.put("validCode", validCode);

		HttpUtils.post(context, url, "", params, new HttpCallBack() {

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

	public static void loginWeiXin(Context context, String city, String country,
								   String headimgurl, String nickName,String openId,
								   String privilege, String sex, String unionId,
								   final WeiXinCallback callback) {

		String url = HTTP_URL + HttpUrl.loginWeiXin;
		// password = MD5Util.MD5(password);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("city", city);
		params.put("country", country);
		params.put("headimgurl", headimgurl);
		params.put("nickName", nickName);
		params.put("openId", openId);
		params.put("privilege", privilege);
		params.put("sex", sex);
		params.put("unionId", unionId);

		HttpUtils.post(context, url, "", params, new HttpCallBack() {

			@Override
			public void success(String content) {
				WeiXinResolve weiXinResolve = new WeiXinResolve(content);
				weiXinResolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}
	public static void modifyPassword(Context context, String verCode,
									  String phone, String password, String loginToken,
									  final NullCallback callback) {

		String url = HTTP_URL + HttpUrl.resetPasswd;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNumber", phone);
		params.put("newPwd", password);
		params.put("validCode", verCode);

		HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {

			@Override
			public void success(String content) {
				NullResolve resolve = new NullResolve(content);
				resolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}

	public static void loginOut(Context context,String loginToken, final NullCallback callback) {

		String url = HTTP_URL + HttpUrl.LoginOut;

		HttpUtils.post(context, url, loginToken, null, new HttpCallBack() {

			@Override
			public void success(String content) {
				NullResolve resolve = new NullResolve(content);
				resolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}
}
