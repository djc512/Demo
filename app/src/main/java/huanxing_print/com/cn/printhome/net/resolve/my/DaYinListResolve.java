package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.net.callback.my.DaYinListCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DaYinListResolve extends BaseResolve <DaYinListBean>{
    public DaYinListResolve(String result) {
        super(result);
    }
    public void resolve(DaYinListCallBack callback) {
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
