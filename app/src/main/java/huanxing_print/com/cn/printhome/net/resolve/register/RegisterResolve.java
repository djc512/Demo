package huanxing_print.com.cn.printhome.net.resolve.register;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.register.RegisterCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class RegisterResolve extends BaseResolve<LoginBean> {

	public RegisterResolve(String result) {
		super(result);
	}

	public void resolve(RegisterCallback callback) {
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
