package huanxing_print.com.cn.printhome.net.resolve.commet;

import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.net.callback.comment.CommentListCallback;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class CommentListResolve extends BaseResolve<CommentListBean> {
    public CommentListResolve(String result) {
        super(result);
    }

    public void resolve(CommentListCallback callback) {
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
