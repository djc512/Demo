package huanxing_print.com.cn.printhome.net.resolve.contact;

import huanxing_print.com.cn.printhome.net.callback.contact.DissolutionQunCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class DissolutionQunResolve extends BaseResolve <String>{
    public DissolutionQunResolve(String result) {
        super(result);
    }
    public void resolve(DissolutionQunCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(bean);
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
