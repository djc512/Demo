package huanxing_print.com.cn.printhome.net.resolve.contact;

import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.FriendSearchCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/8.
 */

public class FriendSearchResolve extends BaseResolve<FriendSearchInfo> {
    public FriendSearchResolve(String result) {
        super(result);
    }

    public void resolve(FriendSearchCallback callback){
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
