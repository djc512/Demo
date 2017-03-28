package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.OrderDetailBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderDetailCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class OrderDetailResolve extends BaseResolve <OrderDetailBean>{
    public OrderDetailResolve(String result) {
        super(result);
    }
    public void resolve(OrderDetailCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(successMsg, bean);
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
