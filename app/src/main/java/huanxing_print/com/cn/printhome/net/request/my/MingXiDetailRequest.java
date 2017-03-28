package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.MingXiDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.MingXiDetailResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class MingXiDetailRequest extends BaseRequst {

    public static void getMxDetail(Context ctx, int pageNum, final MingXiDetailCallBack callBack){

        String mxDetailUrl = HTTP_URL + HttpUrl.mxDetail;
        Map<String,Object> params = new HashMap<>();
        params.put("pageNum",pageNum);

        HttpUtils.getParam(ctx, mxDetailUrl, BaseApplication.getInstance().getLoginToken(), pageNum, new HttpCallBack() {
            @Override
            public void success(String content) {
                MingXiDetailResolve resolve = new MingXiDetailResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
