package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.OrderIdBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderIdCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class OrderIdResolve extends BaseResolve<OrderIdBean> {
    public OrderIdResolve(String result) {
        super(result);
    }
    public void resolve(OrderIdCallBack callback) {
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
