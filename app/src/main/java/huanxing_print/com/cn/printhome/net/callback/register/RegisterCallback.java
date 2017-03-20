package huanxing_print.com.cn.printhome.net.callback.register;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface RegisterCallback extends BaseCallback {
	public void success(LoginBean registerBean);
	public void fail(String errorMsg);
}
