package huanxing_print.com.cn.printhome.net.resolve.contact;

import huanxing_print.com.cn.printhome.net.callback.contact.TransferQunOwnerCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class TransferQunOwnerResolve extends BaseResolve<String> {
    public TransferQunOwnerResolve(String result) {
        super(result);
    }
    public void resolve(TransferQunOwnerCallBack callback) {
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
