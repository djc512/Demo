package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.DebitNormalCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.DebitNormalResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitNormalRequest extends BaseRequst {

//    address	收件地址	string
//    amount	发票金额	string
//    billContext	发票内容	string
//    city	城市	string
//    companyName	发票抬头	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    orderId	订单列表	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    receiver	接收人	string
//    telPhone	联系电话

    public static void sendNormalBack(Context ctx,
                                    String address,
                                    String amount,
                                    String billContext,
                                    String city,
                                    String companyName,
                                    String expAmount,
                                    String fileSize,
                                    String orderId,
                                    int payType,
                                    String receiver,
                                    String telPhone,
                                    final DebitNormalCallBack callBack) {
        String normalUrl = HTTP_URL + HttpUrl.normalDebit;

        Map<String, Object> params = new HashMap<>();

        params.put("address", address);
        params.put("amount", amount);
        params.put("billContext", billContext);
        params.put("city", city);
        params.put("companyName", companyName);
        params.put("expAmount", expAmount);
        params.put("fileSize", fileSize);
        params.put("orderId", orderId);
        params.put("payType", payType);
        params.put("receiver", receiver);
        params.put("telPhone", telPhone);

        HttpUtils.post(ctx, normalUrl, "33b2abe48a76468682880e86b6fa0c2f", params, new HttpCallBack() {
            @Override
            public void success(String content) {
                DebitNormalResolve resolve = new DebitNormalResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
