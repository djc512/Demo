package huanxing_print.com.cn.printhome.net.resolve.contact;

import huanxing_print.com.cn.printhome.net.callback.contact.GroupManagerApprovalCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/8.
 */

public class GroupManagerApprovalResolve extends BaseResolve<String> {
    public GroupManagerApprovalResolve(String result) {
        super(result);
    }

    public void resolve(GroupManagerApprovalCallback callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(successMsg);
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
