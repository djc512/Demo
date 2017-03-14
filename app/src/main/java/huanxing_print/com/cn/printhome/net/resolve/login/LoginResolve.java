package huanxing_print.com.cn.printhome.net.resolve.login;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.ballback.login.LoginCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class LoginResolve extends BaseResolve<LoginBean> {

	public LoginResolve(String result) {
		super(result);
	}

	public void resolve(LoginCallback callback) {
		switch (resultCode) {
		case SUCCESS_CODE:
			callback.success(bean);
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
