package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class Go2DebitResolve extends BaseResolve<String>{
    public Go2DebitResolve(String result) {
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
