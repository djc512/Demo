package huanxing_print.com.cn.printhome.net.resolve.image;

import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class HeadImageUploadResolve extends BaseResolve<HeadImageBean>{

	public HeadImageUploadResolve(String result) {
		super(result);
	}
	

	public void resolve(HeadImageUploadCallback callback) {
		switch (resultCode) {
		case SUCCESS_CODE:
			callback.success(resultMessage,bean);
			break;
		case FAIL_CODE:
			callback.fail(resultMessage);
			break;
		default:
			callback.fail(resultMessage);
			break;

		}
	}

}
