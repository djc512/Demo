package huanxing_print.com.cn.printhome.net.resolve;

import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class NullResolve extends BaseResolve<String> {

	public NullResolve(String result) {
		super(result);
	}

	public void resolve(NullCallback callback) {
		switch (resultCode) {
		case SUCCESS_CODE:
			callback.success(resultMessage);
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
