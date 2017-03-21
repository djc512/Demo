package huanxing_print.com.cn.printhome.net.resolve;

import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class NullResolve extends BaseResolve<String> {

	public NullResolve(String result) {
		super(result);
	}

	public void resolve(NullCallback callback) {
		switch (code) {
		case SUCCESS_CODE:
			callback.success(successMsg);
			break;
		case FAIL_CODE:
			callback.fail(errorMsg);
			break;
		default:
			callback.fail(errorMsg);
			break;
		}

	}

}
