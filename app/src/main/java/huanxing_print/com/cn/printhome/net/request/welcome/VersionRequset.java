package huanxing_print.com.cn.printhome.net.request.welcome;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.welcome.VersionCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.welcome.VersionResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;
import android.content.Context;

public class VersionRequset extends BaseRequst {

	public static void updateVersion(Context context, String version, 
			final VersionCallback callback) {

		String url = HTTP_URL + HttpUrl.versionCheck;

		HttpUtils.getVersionParam(context, url, "", version, new HttpCallBack() {

			@Override
			public void success(String content) {
				VersionResolve versionResolve = new VersionResolve(content);
				versionResolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}


}
