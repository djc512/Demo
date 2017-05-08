package huanxing_print.com.cn.printhome.net.callback.approval;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/8.
 */

public interface QueryApprovalListCallBack extends BaseCallback {
    void success(ArrayList<ApprovalObject> approvalObjects);
}
