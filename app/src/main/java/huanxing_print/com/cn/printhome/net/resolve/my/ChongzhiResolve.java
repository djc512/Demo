package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongzhiResolve extends BaseResolve<ChongZhiBean> {
    public ChongzhiResolve(String result) {
        super(result);
    }

    public void resolve(ChongzhiCallBack callback) {
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
