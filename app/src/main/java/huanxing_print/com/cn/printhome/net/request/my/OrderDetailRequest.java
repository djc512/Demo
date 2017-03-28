package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.OrderDetailResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class OrderDetailRequest extends BaseRequst {

    public static void getOrderDetail (Context ctx, String id, final OrderDetailCallBack callBack){

        String url = HTTP_URL+HttpUrl.orderDetail;
       HttpUtils.getOrderDetail(ctx, url, BaseApplication.getInstance().getLoginToken(), id, new HttpCallBack() {
           @Override
           public void success(String content) {
               OrderDetailResolve resolve = new OrderDetailResolve(content);
               resolve.resolve(callBack);
           }

           @Override
           public void fail(String exception) {
            callBack.connectFail();
           }
       });
    }
}
