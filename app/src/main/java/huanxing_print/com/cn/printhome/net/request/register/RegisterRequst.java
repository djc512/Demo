package huanxing_print.com.cn.printhome.net.request.register;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.register.GetVerCodeCallback;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.register.GetVerCodeResolve;
import huanxing_print.com.cn.printhome.net.resolve.register.RegisterResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;


public class RegisterRequst extends BaseRequst {

	public static void register(Context context, String password, String phone,String wechatId, String verCode,final RegisterCallback callback) {
		String url = HTTP_URL + HttpUrl.register;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNumber", phone);
		params.put("loginPwd", password);
		params.put("validCode", verCode);
		params.put("wechatId", wechatId);

		HttpUtils.post(context, url,"", params, new HttpCallBack() {

			@Override
			public void success(String content) {
				RegisterResolve registerResolve = new RegisterResolve(content);
				registerResolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {

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
			public void success(String content) {
				GetVerCodeResolve getVerCodeResolve = new GetVerCodeResolve(content);
				getVerCodeResolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}
}
