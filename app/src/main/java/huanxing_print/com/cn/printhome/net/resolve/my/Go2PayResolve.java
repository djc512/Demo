package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.Go2PayBean;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class Go2PayResolve extends BaseResolve<Go2PayBean> {
    public Go2PayResolve(String result) {
        super(result);
    }
    public void resolve(Go2PayCallBack callback) {
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
