package huanxing_print.com.cn.printhome.net.resolve.approval;

import huanxing_print.com.cn.printhome.model.approval.UnreadMessage;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryMessageCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class QueryMessageResolve extends BaseResolve<UnreadMessage> {
    public QueryMessageResolve(String result) {
        super(result);
    }

    public void resolve(QueryMessageCallBack callback) {
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
