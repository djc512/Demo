package huanxing_print.com.cn.printhome.net.callback.approval;

import huanxing_print.com.cn.printhome.model.approval.LastApproval;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/9.
 */

public interface QueryLastCallBack extends BaseCallback {
    void success(String msg, LastApproval approval);
}
