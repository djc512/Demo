package huanxing_print.com.cn.printhome.net.resolve.login;

import huanxing_print.com.cn.printhome.model.login.ModifyPasswordBean;
import huanxing_print.com.cn.printhome.net.callback.login.ModifyPasswordCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class ModifyPasswordResolve extends BaseResolve<ModifyPasswordBean> {

	public ModifyPasswordResolve(String result) {
		super(result);
	}

	public void resolve(ModifyPasswordCallback callback) {
		switch (resultCode) {
		case SUCCESS_CODE:
			callback.success(resultMessage, bean);
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
