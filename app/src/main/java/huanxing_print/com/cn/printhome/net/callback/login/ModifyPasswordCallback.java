package huanxing_print.com.cn.printhome.net.callback.login;

import huanxing_print.com.cn.printhome.model.login.ModifyPasswordBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface ModifyPasswordCallback extends BaseCallback {
	public void success(String msg, ModifyPasswordBean bean);
}
