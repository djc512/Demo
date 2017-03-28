package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

public class UpdatePersonInfoRequest extends BaseRequst {

    //更新个人信息
	public static void update(Context context, String loginToken,Map<String, Object> params,
			final NullCallback callback) {
		String url = HTTP_URL + HttpUrl.updateInfo;
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
}
