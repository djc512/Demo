package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.RechargeCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.RechargeResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class RechargeRequset extends BaseRequst {

    public static void getRequest(Context ctx, String id, final RechargeCallBack callBack){
        String url = HTTP_URL+ HttpUrl.queryOrderDetail;

        Map<String,Object> params = new HashMap<>();
        params.put("orderId",id);

        HttpUtils.getOrderDetail(ctx, url, BaseApplication.getInstance().getLoginToken(), id, new HttpCallBack() {
            @Override
            public void success(String content) {
                RechargeResolve resolve = new RechargeResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
