package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class JsCallJava {
    private Context ctx;

    public JsCallJava(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void pay(String orderid) {
        DialogUtils.showProgressDialog(ctx,"正在加载");
        Go2PayRequest.go2PWeChat(ctx, orderid, "JM", new WeChatCallBack() {
            @Override
            public void success(WeChatPayBean bean) {
                DialogUtils.closeProgressDialog();
                PayUtil.getInstance(ctx).weChatPay(bean);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {
                Toast.makeText(ctx, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
