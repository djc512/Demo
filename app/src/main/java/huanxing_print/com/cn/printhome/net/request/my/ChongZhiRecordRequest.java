package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.ChongZhiRecordResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiRecordRequest extends BaseRequst {
    public static void getCzRecord(Context ctx,int pageNum,final ChongZhiRecordCallBack callBack) {

        String czRecordUrl = HTTP_URL + HttpUrl.czRecord;

        HttpUtils.getParam(ctx, czRecordUrl, "33b2abe48a76468682880e86b6fa0c2f",pageNum, new HttpCallBack() {

            @Override
            public void success(String content) {
                ChongZhiRecordResolve resolve = new ChongZhiRecordResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
