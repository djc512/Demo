package huanxing_print.com.cn.printhome.net.callback.welcome;

import huanxing_print.com.cn.printhome.model.welcome.GetVersionBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface VersionCallback extends BaseCallback {
	
	public void success(GetVersionBean loginBean);
	
}
