package huanxing_print.com.cn.printhome.net.request.image;

import java.io.File;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.image.HeadImageUploadResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

import android.content.Context;

public class HeadImageUploadRequest extends BaseRequst {
	public static void upload(Context context, String sessionId, String userId, File file,
			final HeadImageUploadCallback callback) {

		String url = HTTP_URL + HttpUrl.fileUpload;

		HttpUtils.postFile(context, url, sessionId, userId, file, new HttpCallBack() {

			@Override
			public void success(String content) {
				HeadImageUploadResolve resolve = new HeadImageUploadResolve(content);
				resolve.resolve(callback);

			}

			@Override
			public void fail(String exception) {
				callback.connectFail();
			}
		});
	}
}
