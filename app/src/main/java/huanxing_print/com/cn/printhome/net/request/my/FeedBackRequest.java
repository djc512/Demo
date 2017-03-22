package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.FeedBackCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.FeedBackResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class FeedBackRequest extends BaseRequst {

    public static void sendFeedBack(Context ctx, String feedBack, final FeedBackCallBack callBack) {
        String feeBackUrl = HTTP_URL + HttpUrl.feedBack;

        Map<String, Object> params = new HashMap<>();
        params.put("remark", feedBack);

        HttpUtils.post(ctx, feeBackUrl, "33b2abe48a76468682880e86b6fa0c2f", params, new HttpCallBack() {
            @Override
            public void success(String content) {
                FeedBackResolve resolve = new FeedBackResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
