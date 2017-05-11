package huanxing_print.com.cn.printhome.net.request.commet;

import android.content.Context;
import android.util.Log;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.comment.CommentListCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.commet.CommentListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class CommentListRequest extends BaseRequst {

    public static void request(Context ctx, int pageNum, int printno, int type, final CommentListCallback callBack) {

        String commentlistUrl = HTTP_URL + HttpUrl.getCommentList;

        HttpUtils.getCommentlist(ctx, commentlistUrl, BaseApplication.getInstance().getLoginToken(), pageNum, printno, type, new HttpCallBack() {

            @Override
            public void success(String content) {
                Log.d("CommentListRequest", content);
                CommentListResolve resolve = new CommentListResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
