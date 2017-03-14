package huanxing_print.com.cn.printhome.net.ballback.login;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.ballback.BaseCallback;


public interface LoginCallback extends BaseCallback {
	
	public void success(LoginBean loginBean);
	
}
