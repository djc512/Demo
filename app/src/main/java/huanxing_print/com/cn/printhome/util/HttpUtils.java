package huanxing_print.com.cn.printhome.util;

import java.io.File;
import java.util.Map;

import com.google.gson.GsonBuilder;
import huanxing_print.com.cn.printhome.constant.Config;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.util.encrypt.Base64;
import huanxing_print.com.cn.printhome.util.encrypt.RSAEncrypt;
import huanxing_print.com.cn.printhome.util.encrypt.SHAUtils;
import huanxing_print.com.cn.printhome.util.time.TimeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class HttpUtils {

	private final static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO++7KywM8g/51KQacb4GRC7fJDZLU1KdlZJwB6ROy7As7bSr8H2q4INtOu12uBNPVvozOVdoanybgvTiISyvj1pw8a5/fgYAbdiUam3FWkoKCI9v2rxJMEhvql+aMws7diCXqovnMgT3AO0hIrTK5o9+dfvpveeTbkPDhGSkSlQIDAQAB";

	public HttpUtils() {

	}

	public static void post(Object obj, final String url, String sessionId, Map<String, Object> params,
			final HttpCallBack callback) {
		String paramsStr = new GsonBuilder().serializeNulls().create().toJson(params);
		Logger.d("http-request:" + url + "----" + sessionId + "----" + paramsStr);
		TimeUtils.beginTime();
		OkHttpUtils.post().url(url).addParams("param", paramsStr).addParams("sessionId", sessionId)
				.addParams("sig", sig("param=" + paramsStr + "&sessionId=" + sessionId)).tag(obj).build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String result, int arg1) {
						TimeUtils.endTime();
						Logger.d("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
						callback.success(result);
					}

					@Override
					public void onError(Call call, Exception exception, int arg2) {
						TimeUtils.endTime();
						Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
						String message = exception.getMessage();
						if ("Socket closed".equalsIgnoreCase(message)) {// 取消请求

						} else {
							callback.fail(exception.getMessage());
						}

					}
				});
	}

	public static void postFile(Object obj, final String url, String sessionId, String userId, File file,
			final HttpCallBack callback) {
		Logger.d("http-request:" + url + "----" + sessionId + "----" + file.getName());
		String sig = sig("sessionId=" + sessionId + "&userId=" + userId);

		OkHttpUtils.post().url(url).addParams("sessionId", sessionId).addParams("userId", userId).addParams("sig", sig)
				.addFile("file", file.getName(), file).tag(obj).build().connTimeOut(Config.FILE_CONNECT_TIME_OUT)
				.readTimeOut(Config.FILE_CONNECT_TIME_OUT).writeTimeOut(Config.FILE_CONNECT_TIME_OUT)
				.execute(new StringCallback() {

					@Override
					public void onResponse(String result, int arg1) {
						TimeUtils.endTime();
						Logger.d("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
						callback.success(result);
					}

					@Override
					public void onError(Call call, Exception exception, int arg2) {
						TimeUtils.endTime();
						Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
						String message = exception.getMessage();
						if ("Socket closed".equalsIgnoreCase(message)) {// 取消请求

						} else {
							callback.fail(exception.getMessage());
						}

					}
				});
	}

	public static void postFiles(Object obj, final String url, String sig, Map<String, Object> params,
			Map<String, File> files, final HttpCallBack callback) {
		StringBuilder sb = new StringBuilder();
		PostFormBuilder postFormBuilder = OkHttpUtils.post().url(url);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			postFormBuilder.addParams(key, value.toString());
			sb.append(key + "=" + value.toString() + "&");
			Logger.d("postFiles:" + key + "---" + value);
		}
		for (Map.Entry<String, File> entry : files.entrySet()) {
			String key = entry.getKey();
			File file = entry.getValue();
			postFormBuilder.addFile(key, file.getName(), file);
		}
		sb = sb.deleteCharAt(sb.lastIndexOf("&"));
		Logger.d("http-request:" + url + "----" + sb.toString());

		postFormBuilder.addParams("sig", sig(sig)).tag(obj).build().connTimeOut(Config.FILE_CONNECT_TIME_OUT)
				.readTimeOut(Config.FILE_CONNECT_TIME_OUT).writeTimeOut(Config.FILE_CONNECT_TIME_OUT)
				.execute(new StringCallback() {

					@Override
					public void onResponse(String result, int arg1) {
						TimeUtils.endTime();
						Logger.d("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
						callback.success(result);
					}

					@Override
					public void onError(Call call, Exception exception, int arg2) {
						TimeUtils.endTime();
						Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
						String message = exception.getMessage();
						if ("Socket closed".equalsIgnoreCase(message)) {// 取消请求

						} else {
							callback.fail(exception.getMessage());
						}

					}
				});
	}

	private static String sig(String value) {
		try {
			value = SHAUtils.SHA1(Base64.encode(value.getBytes()));
			return Base64.encode(RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(PUBLIC_KEY), value.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
