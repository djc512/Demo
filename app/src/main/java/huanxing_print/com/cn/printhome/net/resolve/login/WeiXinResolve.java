package huanxing_print.com.cn.printhome.net.resolve.login;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.login.WeiXinCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class WeiXinResolve extends BaseResolve<LoginBean> {

	public WeiXinResolve(String result) {
		super(result);
	}

	public void resolve(WeiXinCallback callback) {
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
