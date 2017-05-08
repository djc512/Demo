package huanxing_print.com.cn.printhome.net.resolve.contact;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by wanghao on 2017/5/8.
 */

public class GroupListResolve extends BaseResolve<ArrayList<GroupInfo>>{
    public GroupListResolve(String result) {
        super(result);
    }

    public void resolve(GroupListCallback callback) {
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
