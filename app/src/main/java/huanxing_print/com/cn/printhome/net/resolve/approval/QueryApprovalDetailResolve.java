package huanxing_print.com.cn.printhome.net.resolve.approval;

import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class QueryApprovalDetailResolve extends BaseResolve<ApprovalDetail> {
    public QueryApprovalDetailResolve(String result) {
        super(result);
    }

    public void resolve(QueryApprovalDetailCallBack callback) {
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
