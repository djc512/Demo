package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.net.callback.my.DebitValueCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitValueResolve extends BaseResolve<String> {
    public DebitValueResolve(String result) {
        super(result);
    }

    public void resolve(DebitValueCallBack callback) {
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
