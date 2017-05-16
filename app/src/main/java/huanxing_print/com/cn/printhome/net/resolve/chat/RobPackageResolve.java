package huanxing_print.com.cn.printhome.net.resolve.chat;

import huanxing_print.com.cn.printhome.net.callback.chat.RobPackageCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class RobPackageResolve extends BaseResolve<String> {
    public RobPackageResolve(String result) {
        super(result);
    }

    public void resolve(RobPackageCallBack callback) {
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
