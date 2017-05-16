package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;
import android.util.Log;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderListCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.OrderListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/16 0016.
 */
public class OrderListRequest extends BaseRequst {

    public static void request(Context ctx, String pageNum, final OrderListCallBack callBack) {

        String url = HTTP_URL + HttpUrl.orderList;
        HttpUtils.getOrderList(ctx, url, BaseApplication.getInstance().getLoginToken(), pageNum, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("OrderListRequest", content);
                OrderListResolve resolve = new OrderListResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
