package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoResolve extends BaseResolve<MyInfoBean> {
    public MyInfoResolve(String result) {
        super(result);
    }

    public void resolve(MyInfoCallBack callback) {
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
