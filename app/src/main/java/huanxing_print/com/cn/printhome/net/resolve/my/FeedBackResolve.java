package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.FeedBackBean;
import huanxing_print.com.cn.printhome.net.callback.my.FeedBackCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class FeedBackResolve extends BaseResolve<FeedBackBean> {
    public FeedBackResolve(String result) {
        super(result);
    }

    public void resolve(FeedBackCallBack callback) {
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
