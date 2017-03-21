package huanxing_print.com.cn.printhome.net.callback.register;

import huanxing_print.com.cn.printhome.net.callback.BaseCallback;
public interface GetVerCodeCallback extends BaseCallback {
	
	public void success(String msg);
	public void fail(String msg);
}
