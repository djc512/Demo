package huanxing_print.com.cn.printhome.net.resolve.chat;

import huanxing_print.com.cn.printhome.model.chat.RedPackage;
import huanxing_print.com.cn.printhome.net.callback.chat.SendPackageCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class SendPackageResolve extends BaseResolve<RedPackage> {
    public SendPackageResolve(String result) {
        super(result);
    }

    public void resolve(SendPackageCallBack callback) {
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
