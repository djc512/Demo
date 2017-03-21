package huanxing_print.com.cn.printhome.net.resolve.login;

import huanxing_print.com.cn.printhome.model.login.VeryCodeBean;
import huanxing_print.com.cn.printhome.net.callback.login.VeryCodeCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class VeryCodeResolve extends BaseResolve<VeryCodeBean> {
    public VeryCodeResolve(String result) {
        super(result);
    }
    public void resolve(VeryCodeCallback callback) {
        switch (code){
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
