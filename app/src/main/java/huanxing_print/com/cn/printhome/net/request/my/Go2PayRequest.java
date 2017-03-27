package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.Go2PayResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class Go2PayRequest extends BaseRequst {

    public static void go2Pay(Context ctx, String orderId, String orderType, final Go2PayCallBack callBack){
        String payUrl = HTTP_URL + HttpUrl.doPay;
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("orderType",orderType);

        HttpUtils.post(ctx, payUrl, BaseApplication.getInstance().getLoginToken(), params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Go2PayResolve resolve = new Go2PayResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
