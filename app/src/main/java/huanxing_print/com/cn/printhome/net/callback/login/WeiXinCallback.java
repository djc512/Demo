package huanxing_print.com.cn.printhome.net.callback.login;

import huanxing_print.com.cn.printhome.model.login.LoginBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface WeiXinCallback extends BaseCallback {
	
	public void success(LoginBean loginBean);
	
}
