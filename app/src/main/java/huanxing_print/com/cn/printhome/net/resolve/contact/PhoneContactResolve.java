package huanxing_print.com.cn.printhome.net.resolve.contact;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.PhoneContactCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/9.
 */

public class PhoneContactResolve extends BaseResolve<ArrayList<FriendSearchInfo>>{
    public PhoneContactResolve(String result) {
        super(result);
    }

    public void resolve(PhoneContactCallback callback) {
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
