package huanxing_print.com.cn.printhome.net.resolve.approval;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalListCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class QueryApprovalListResolve extends BaseResolve<ArrayList<ApprovalObject>> {
    public QueryApprovalListResolve(String result) {
        super(result);
    }

    public void resolve(QueryApprovalListCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(successMsg,bean);
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
