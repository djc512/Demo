package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.DebitValueCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.DebitValueResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitValueRequest extends BaseRequst {

//    address	收件人地址	string
//    amount	发票金额	string
//    bankAccount	开户行账号	string
//    bankName	开户行	string
//    billContext	发票内容0-打印费	number
//    city	城市	string
//    companyAddress	发票公司地址	string
//    companyName	发票公司名称	string
//    companyPhone	发票公司电话	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    orderId	订单ID列表	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    ratepayerId	纳税人识别码	string
//    receiver	快递接收人	string
//    telPhone	联系人号码	string

    public static void sendValueBack(Context ctx,
                                    String address,
                                    String amount,
                                    String bankAccount,
                                    String bankName,
                                    String billContext,
                                    String city,
                                    String companyAddress,
                                    String companyName,
                                    String companyPhone,
                                    String expAmount,
                                    String fileSize,
                                    String orderId,
                                    int payType,
                                    int ratepayerId,
                                    String receiver,
                                    String telPhone,
                                    final DebitValueCallBack callBack) {
        String valueUrl = HTTP_URL + HttpUrl.normalDebit;

        Map<String, Object> params = new HashMap<>();
        
        params.put("address", address);
        params.put("amount", amount);
        params.put("amount", bankAccount);
        params.put("amount", bankName);
        params.put("billContext", billContext);
        params.put("city", city);
        params.put("city", companyAddress);
        params.put("companyName", companyName);
        params.put("companyName", companyPhone);
        params.put("expAmount", expAmount);
        params.put("fileSize", fileSize);
        params.put("orderId", orderId);
        params.put("payType", payType);
        params.put("payType", ratepayerId);
        params.put("receiver", receiver);
        params.put("telPhone", telPhone);

        HttpUtils.post(ctx, valueUrl, "33b2abe48a76468682880e86b6fa0c2f", params, new HttpCallBack() {
            @Override
            public void success(String content) {
                DebitValueResolve resolve = new DebitValueResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
