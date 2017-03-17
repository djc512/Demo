package huanxing_print.com.cn.printhome.net.callback.image;

import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface HeadImageUploadCallback extends BaseCallback {

	public void success(String msg, HeadImageBean bean);

}
