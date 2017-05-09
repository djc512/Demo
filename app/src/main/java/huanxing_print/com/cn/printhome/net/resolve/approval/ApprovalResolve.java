package huanxing_print.com.cn.printhome.net.resolve.approval;

import huanxing_print.com.cn.printhome.net.callback.approval.ApprovalCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class ApprovalResolve extends BaseResolve<String> {
    public ApprovalResolve(String result) {
        super(result);
    }

    public void resolve(ApprovalCallBack callback) {
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
