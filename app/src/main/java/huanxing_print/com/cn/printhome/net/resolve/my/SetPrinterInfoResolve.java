package huanxing_print.com.cn.printhome.net.resolve.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.my.PrinterInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.SetPrinterInfoCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;


public class SetPrinterInfoResolve extends BaseResolve<List<PrinterInfoBean>> {

	public SetPrinterInfoResolve(String result) {
		super(result);
	}

	public void resolve(SetPrinterInfoCallback callback) {
		switch (code) {
		case SUCCESS_CODE:
			callback.success(bean);
			break;
		case FAIL_CODE:
			callback.fail(errorMsg);
			break;
		default:
			callback.fail(errorMsg);
			break;
		}

	}

}
