package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.DaYinListCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.DaYinListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DaYinListRequest extends BaseRequst {
    public static void getDaYinList(Context ctx, int pageNum, final DaYinListCallBack callBack){
        String url = HTTP_URL+ HttpUrl.dyList;
        Map<String,Object> params = new HashMap<>();

        params.put("pageNum",pageNum);

        HttpUtils.getParam(ctx, url, BaseApplication.getInstance().getLoginToken(), pageNum, new HttpCallBack() {
            @Override
            public void success(String content) {
                DaYinListResolve resolve = new DaYinListResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
