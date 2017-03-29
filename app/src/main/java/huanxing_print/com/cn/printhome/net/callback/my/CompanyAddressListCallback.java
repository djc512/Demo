package huanxing_print.com.cn.printhome.net.callback.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.address.ProvinceModel;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;
public interface CompanyAddressListCallback extends BaseCallback {
	public void success(List<ProvinceModel> bean);
}
