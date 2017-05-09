package huanxing_print.com.cn.printhome.net.resolve.approval;

import huanxing_print.com.cn.printhome.net.callback.approval.CheckVoucherCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class CheckVoucherResolve extends BaseResolve<String> {
    public CheckVoucherResolve(String result) {
        super(result);
    }

    public void resolve(CheckVoucherCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(successMsg, data);
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
