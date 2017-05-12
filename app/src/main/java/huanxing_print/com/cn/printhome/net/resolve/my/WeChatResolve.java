package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class WeChatResolve extends BaseResolve<WeChatPayBean> {
    public WeChatResolve(String result) {
        super(result);
    }

    public void resolve(WeChatCallBack callback) {
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
