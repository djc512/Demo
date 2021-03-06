package huanxing_print.com.cn.printhome.net.request.image;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.image.HeadImageUploadResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

import android.content.Context;

public class HeadImageUploadRequest extends BaseRequst {
	public static void upload(Context context,Map<String, Object> params,final HeadImageUploadCallback callback) {

		String url = HTTP_URL + HttpUrl.fileUpload;
		//单张图片上传
		HttpUtils.postFile(context, url,  params, new HttpCallBack() {

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
	//批量图片上传
}
