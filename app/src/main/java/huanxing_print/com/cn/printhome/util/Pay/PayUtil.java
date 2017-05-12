package huanxing_print.com.cn.printhome.util.Pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import huanxing_print.com.cn.printhome.model.my.AuthResult;
import huanxing_print.com.cn.printhome.model.my.PayResult;
import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;

import static huanxing_print.com.cn.printhome.base.BaseApplication.WX_APPID;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class PayUtil {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private static Context mContext;
    private static PayUtil singleton = null;
    private static IWXAPI mWxApi;

    private PayUtil() {
    }

    public static synchronized PayUtil getInstance(Context ctx) {
        mContext = ctx;
        if (singleton == null) {
            singleton = new PayUtil();
        }
        return singleton;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();

                        if (null != callBack) {
                            callBack.paySuccess();
                        }
                        //修改账户余额
//                        EventBus.getDefault().post(rechargeAmout, "rechargeAmout");

//                        finishCurrentActivity();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

                        if (null != callBack) {
                            callBack.payFailed();
                        }
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(mContext,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(mContext,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * 支付宝支付
     *
     * @param id
     */
    public void alipay(final long id) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(id + "", true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public interface PayCallBack {
        void paySuccess();

        void payFailed();
    }

    public void setCallBack(PayCallBack callBack) {
        this.callBack = callBack;
    }

    private PayCallBack callBack;

    public static void weChatPay(WeChatPayBean bean) {
        mWxApi = WXAPIFactory.createWXAPI(mContext, WX_APPID, true);
        mWxApi.registerApp(WX_APPID);

        if (mWxApi != null) {
            PayReq req = new PayReq();
            req.appId = WX_APPID;// 微信开放平台审核通过的应用APPID
            req.partnerId = bean.getPartnerId();// 微信支付分配的商户号
            req.prepayId = bean.getPrepayId();// 预支付订单号，app服务器调用“统一下单”接口获取
            req.nonceStr = bean.getNonceStr();// 随机字符串，不长于32位，服务器小哥会给咱生成
            req.timeStamp = bean.getTimeStamp();// 时间戳，app服务器小哥给出
            req.packageValue = bean.getPkg();// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
            req.sign = bean.getPaySign();// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
            mWxApi.sendReq(req);
        }
    }
}
