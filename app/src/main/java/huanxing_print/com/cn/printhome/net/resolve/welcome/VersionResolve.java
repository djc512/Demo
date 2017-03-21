package huanxing_print.com.cn.printhome.net.resolve.welcome;

import huanxing_print.com.cn.printhome.model.welcome.GetVersionBean;
import huanxing_print.com.cn.printhome.net.callback.welcome.VersionCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class VersionResolve extends BaseResolve<GetVersionBean> {

	public VersionResolve(String result) {
		super(result);
	}

	public void resolve(VersionCallback callback) {
		switch (code) {
		case SUCCESS_CODE:
			callback.success(bean);
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
