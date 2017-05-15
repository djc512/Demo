package huanxing_print.com.cn.printhome.net.resolve.chat;

import huanxing_print.com.cn.printhome.net.callback.chat.SendCommonPackageCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class SendCommonPackageResolve extends BaseResolve<String> {
    public SendCommonPackageResolve(String result) {
        super(result);
    }

    public void resolve(SendCommonPackageCallBack callback) {
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
