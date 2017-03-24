package huanxing_print.com.cn.printhome.net.callback.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.my.PrinterInfoBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;


public interface SetPrinterInfoCallback extends BaseCallback {
	
	public void success(List<PrinterInfoBean> bean);
	
}
