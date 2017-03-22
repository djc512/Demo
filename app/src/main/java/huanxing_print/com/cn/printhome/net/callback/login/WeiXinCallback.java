package huanxing_print.com.cn.printhome.net.callback.login;

import huanxing_print.com.cn.printhome.model.login.WeiXinBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface WeiXinCallback extends BaseCallback {
	
	public void success(WeiXinBean bean);
	
}
