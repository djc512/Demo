package huanxing_print.com.cn.printhome.net.resolve.contact;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.NewFriendCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/8.
 */

public class NewFriendResolve extends BaseResolve<ArrayList<NewFriendInfo>>{
    public NewFriendResolve(String result) {
        super(result);
    }

    public void resolve(NewFriendCallback callback) {
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
