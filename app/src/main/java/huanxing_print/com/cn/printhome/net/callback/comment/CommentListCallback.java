package huanxing_print.com.cn.printhome.net.callback.comment;

import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public abstract class CommentListCallback implements BaseCallback {
    public abstract void success(CommentListBean bean);
}
