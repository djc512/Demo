package huanxing_print.com.cn.printhome.net.resolve.my;

import huanxing_print.com.cn.printhome.model.my.UserInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.UserInfoCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UserInfoResolve extends BaseResolve<UserInfoBean> {
    public UserInfoResolve(String result) {
        super(result);
    }

    public void resolve(UserInfoCallBack callback) {
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
