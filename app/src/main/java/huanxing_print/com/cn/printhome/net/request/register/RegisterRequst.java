package huanxing_print.com.cn.printhome.net.request.register;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.request.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.register.GetVerCodeResolve;
import huanxing_print.com.cn.printhome.net.resolve.register.RegisterResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

import android.content.Context;


public class RegisterRequst extends BaseRequst {

	public static void register(Context context, String password, String phone, String verCode,final RegisterCallback callback) {
		String url = HTTP_URL + HttpUrl.register;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNumber", phone);
		params.put("loginPwd", password);
		params.put("validCode", verCode);

		HttpUtils.post(context, url,"", params, new HttpCallBack() {

			@Override
			public void onSucceed(String content) {
				RegisterResolve registerResolve = new RegisterResolve(content);
				registerResolve.resolve(callback);

			}

			@Override
			public void onFailed(String exception) {

			}
		});
	}

	public static void getVerCode(Context context, String loginToken, String phoneCode, int type, final GetVerCodeCallback callback) {
		String url = HTTP_URL + HttpUrl.getVeryCode;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNumber", phoneCode);
		params.put("type", type);
		HttpUtils.post(context, url,loginToken , params, new HttpCallBack() {

			@Override
			public void onSucceed(String content) {
				GetVerCodeResolve getVerCodeResolve = new GetVerCodeResolve(content);
				getVerCodeResolve.resolve(callback);

			}

			@Override
			public void onFailed(String exception) {
				callback.connectFail();
			}
		});
	}
}
