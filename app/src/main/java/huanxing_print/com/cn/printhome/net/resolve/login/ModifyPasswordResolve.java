package huanxing_print.com.cn.printhome.net.resolve.login;

import huanxing_print.com.cn.printhome.model.login.ModifyPasswordBean;
import huanxing_print.com.cn.printhome.net.callback.login.ModifyPasswordCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class ModifyPasswordResolve extends BaseResolve<ModifyPasswordBean> {

	public ModifyPasswordResolve(String result) {
		super(result);
	}

	public void resolve(ModifyPasswordCallback callback) {
		switch (code) {
		case SUCCESS_CODE:
			callback.success(successMsg, bean);
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
