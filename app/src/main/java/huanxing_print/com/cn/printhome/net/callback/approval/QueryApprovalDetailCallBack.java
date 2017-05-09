package huanxing_print.com.cn.printhome.net.callback.approval;

import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/8.
 */

public interface QueryApprovalDetailCallBack extends BaseCallback {
    void success(String msg, ApprovalDetail approvalDetail);
}
