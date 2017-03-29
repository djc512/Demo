package huanxing_print.com.cn.printhome.net.resolve.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.address.ProvinceModel;
import huanxing_print.com.cn.printhome.net.callback.my.CompanyAddressListCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

public class CompanyAddressListResolve extends BaseResolve<List<ProvinceModel>> {

	public CompanyAddressListResolve(String result) {
		super(result);
	}

	public void resolve(CompanyAddressListCallback callback) {
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
