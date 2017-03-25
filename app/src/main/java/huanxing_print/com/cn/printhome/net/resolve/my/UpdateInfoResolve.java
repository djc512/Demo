package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.UpdateInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.UpdateInfoCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by DjC512 on 2017-3-25.
 */

public class UpdateInfoResolve extends BaseResolve<UpdateInfoBean> {
    public UpdateInfoResolve(String result) {
        super(result);
    }

    public void resolve(UpdateInfoCallBack callback) {
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
