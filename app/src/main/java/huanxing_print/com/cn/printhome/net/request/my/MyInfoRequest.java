package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.MyInfoResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoRequest extends BaseRequst {

    public static void getMyInfo(Context ctx, final MyInfoCallBack callBack) {
        String myInfoUrl = HTTP_URL + HttpUrl.myinfo;

        HttpUtils.get(ctx, myInfoUrl, "33b2abe48a76468682880e86b6fa0c2f", new HttpCallBack() {
            @Override
            public void success(String content) {
                MyInfoResolve resolve = new MyInfoResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
