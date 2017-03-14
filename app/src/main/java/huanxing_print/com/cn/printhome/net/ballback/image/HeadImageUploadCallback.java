package huanxing_print.com.cn.printhome.net.ballback.image;

import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.ballback.BaseCallback;


public interface HeadImageUploadCallback extends BaseCallback {

	public void success(String msg, HeadImageBean bean);

}
