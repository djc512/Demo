package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;
import android.util.Log;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.ADCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.ADListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
public class ADListRequest extends BaseRequst {

    public static void request(Context ctx, final ADCallBack callBack) {

        String url = HTTP_URL + HttpUrl.printBanner;
        HttpUtils.getLastApprovalParam(ctx, url, BaseApplication.getInstance().getLoginToken(), 1, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("OrderListRequest", content);
                ADListResolve resolve = new ADListResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
