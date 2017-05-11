package huanxing_print.com.cn.printhome.net.request.commet;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class CommentRequest extends BaseRequst {
    /**
     * 提交评论
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void submit(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String exitGroupUrl = HTTP_URL + HttpUrl.submitComment;

        HttpUtils.post(ctx, exitGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "submitComment" + content);
                NullResolve resolve = new NullResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }
}
