package huanxing_print.com.cn.printhome.net.resolve.approval;

import huanxing_print.com.cn.printhome.model.approval.LastApprover;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryLastCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class LastApprovalResolve extends BaseResolve<LastApprover> {
    public LastApprovalResolve(String result) {
        super(result);
    }

    public void resolve(QueryLastCallBack callback) {
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
