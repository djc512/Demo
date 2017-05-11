package huanxing_print.com.cn.printhome.net.request.commet;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.commet.UploadPicResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class UpLoadPicRequest extends BaseRequst {
    /**
     * 上传图片
     * @param ctx
     * @param params
     * @param callBack
     */
    public static void request(Context ctx, Map<String, Object> params, final UpLoadPicCallBack callBack) {
        String batchFileUploadUrl = HTTP_URL + HttpUrl.batchFileUpload;

        HttpUtils.post(ctx, batchFileUploadUrl, BaseApplication.getInstance().getLoginToken(), params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("UpLoadPicRequest", content);
                UploadPicResolve resolve = new UploadPicResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
