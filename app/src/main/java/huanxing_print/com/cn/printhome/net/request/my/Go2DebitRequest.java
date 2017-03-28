package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.Go2PayResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class Go2DebitRequest extends BaseRequst {

    public static void getRequset(Context ctx, final Go2PayCallBack callBack){

        String url = HTTP_URL + HttpUrl.go2Debit;

        HttpUtils.get(ctx, url, BaseApplication.getInstance().getLoginToken(), new HttpCallBack() {
            @Override
            public void success(String content) {
                Go2PayResolve resolve = new Go2PayResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }
}
