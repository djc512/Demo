package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.my.SetPrinterInfoCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.my.SetPrinterInfoResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class SetDefaultPrinterRequest extends BaseRequst {
    /**
     * 获取用户信息
     */
    public static void getPrinterList(Context ctx,String loginToken, final SetPrinterInfoCallback callback){
        String url = HTTP_URL+ HttpUrl.printerList;

      HttpUtils.get(ctx, url, loginToken, new HttpCallBack() {
          @Override
          public void success(String content) {
              SetPrinterInfoResolve resolve = new SetPrinterInfoResolve(content);
              resolve.resolve(callback);
          }

          @Override
          public void fail(String exception) {
            callback.connectFail();
          }
      });
    }

    public static void setDefaultPrinter(Context context, String printerNo, String loginToken,
                                      final NullCallback callback) {

        String url = HTTP_URL + HttpUrl.setDefaultprinter;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("printerNo", printerNo);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {

            @Override
            public void success(String content) {
                NullResolve resolve = new NullResolve(content);
                resolve.resolve(callback);

            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }

}
