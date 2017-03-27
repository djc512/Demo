package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.OrderIdCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.OrderIdResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class OrderIdRequest extends BaseRequst {

    public static void getOrderId(Context ctx, String money, final OrderIdCallBack callBack){

        String orderIdurl = HTTP_URL + HttpUrl.payOrderId;

        Map<String,Object> params = new HashMap<>();
        params.put("amount",money);

        HttpUtils.post(ctx, orderIdurl, "33b2abe48a76468682880e86b6fa0c2f", params, new HttpCallBack() {
            @Override
            public void success(String content) {
                OrderIdResolve resolve = new OrderIdResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }
}
