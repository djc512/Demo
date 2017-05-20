package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.webkit.JavascriptInterface;

import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class JsCallJava {
    private Context ctx;

    public JsCallJava(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void callWechatPay(WeChatPayBean bean) {
        PayUtil.getInstance(ctx).weChatPay(bean);
    }
}
