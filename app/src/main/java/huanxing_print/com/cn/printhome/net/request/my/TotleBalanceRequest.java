package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;
import android.util.Log;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.TotleBalanceCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.TotleBalanceResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TotleBalanceRequest extends BaseRequst {
    public static void request(Context ctx, final TotleBalanceCallBack callBack) {

        String chongzhiUrl = HTTP_URL + HttpUrl.totlebanlance;

        HttpUtils.get(ctx, chongzhiUrl, BaseApplication.getInstance().getLoginToken(), new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("ChongzhiRequest", content);
                TotleBalanceResolve resolve = new TotleBalanceResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
