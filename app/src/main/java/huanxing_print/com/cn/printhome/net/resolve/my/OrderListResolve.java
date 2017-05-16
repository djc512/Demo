package huanxing_print.com.cn.printhome.net.resolve.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.my.OrderListBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderListCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class OrderListResolve extends BaseResolve<List<OrderListBean>> {
    public OrderListResolve(String result) {
        super(result);
    }

    public void resolve(OrderListCallBack callback) {
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
