package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.TotleBalanceBean;
import huanxing_print.com.cn.printhome.net.callback.my.TotleBalanceCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TotleBalanceResolve extends BaseResolve<TotleBalanceBean> {
    public TotleBalanceResolve(String result) {
        super(result);
    }

    public void resolve(TotleBalanceCallBack callback) {
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
