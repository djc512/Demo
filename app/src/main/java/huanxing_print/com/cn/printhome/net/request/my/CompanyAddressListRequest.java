package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.CompanyAddressListCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.CompanyAddressListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

public class CompanyAddressListRequest extends BaseRequst {
	public static void verify(Context context, String loginToken,
			final CompanyAddressListCallback callback) {
		String url = HTTP_URL + HttpUrl.getAreaInfo;
		HttpUtils.get(context, url, loginToken, new HttpCallBack() {

			@Override
			public void success(String content) {
				CompanyAddressListResolve resolve = new CompanyAddressListResolve(content);
				resolve.resolve(callback);
			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}

}
