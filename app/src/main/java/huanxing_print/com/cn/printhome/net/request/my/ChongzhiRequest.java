package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.ChongzhiResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongzhiRequest extends BaseRequst{

    public static void getChongZhi(Context ctx, final ChongzhiCallBack callBack){

        String chongzhiUrl = HttpUrl.test+ HttpUrl.chongzhi;

        HttpUtils.get(ctx, chongzhiUrl, BaseApplication.getInstance().getLoginToken(),new HttpCallBack() {
            @Override
            public void success(String content) {
                ChongzhiResolve resolve = new ChongzhiResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
