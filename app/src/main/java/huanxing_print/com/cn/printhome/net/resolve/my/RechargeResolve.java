package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.RechargeBean;
import huanxing_print.com.cn.printhome.net.callback.my.RechargeCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class RechargeResolve extends BaseResolve<RechargeBean> {
    public RechargeResolve(String result) {
        super(result);
    }

    public void resolve(RechargeCallBack callback) {
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
