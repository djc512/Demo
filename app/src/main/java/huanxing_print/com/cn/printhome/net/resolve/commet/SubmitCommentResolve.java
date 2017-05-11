package huanxing_print.com.cn.printhome.net.resolve.commet;

import huanxing_print.com.cn.printhome.net.callback.comment.SubmitCommentCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class SubmitCommentResolve extends BaseResolve<String> {
    public SubmitCommentResolve(String result) {
        super(result);
    }

    public void resolve(SubmitCommentCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(bean);
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
