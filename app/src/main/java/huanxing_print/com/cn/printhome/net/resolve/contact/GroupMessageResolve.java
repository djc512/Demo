package huanxing_print.com.cn.printhome.net.resolve.contact;

import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/10.
 */

public class GroupMessageResolve extends BaseResolve<GroupMessageInfo>{
    public GroupMessageResolve(String result) {
        super(result);
    }

    public void resolve(GroupMessageCallback callback) {
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
