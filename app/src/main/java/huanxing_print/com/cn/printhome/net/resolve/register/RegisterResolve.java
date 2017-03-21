package huanxing_print.com.cn.printhome.net.resolve.register;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class RegisterResolve extends BaseResolve<LoginBean> {

	public RegisterResolve(String result) {
		super(result);
	}

	public void resolve(RegisterCallback callback) {
		switch (errorCode) {
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
