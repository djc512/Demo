package huanxing_print.com.cn.printhome.net.request.print;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.util.time.TimeUtils;
import okhttp3.Call;

/**
 * Created by LGH on 2017/3/20.
 */

public class Http {

    public static void post(Object obj, final String url, Map<String, Object> params,
                            final HttpCallBack callback) {
        String paramsStr = new GsonBuilder().serializeNulls().create().toJson(params);
        Logger.d("http-request:" + url  + "----" + paramsStr);
        TimeUtils.beginTime();
        OkHttpUtils.post().url(url).addParams("param", paramsStr).addHeader("apiversion", "1").addHeader("platform", "android").tag(obj).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String result, int arg1) {
                        TimeUtils.endTime();
                        Logger.d("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
                        callback.success(result);
                    }

                    @Override
                    public void onError(Call call, Exception exception, int arg2) {
                        TimeUtils.endTime();
                        Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
                        String message = exception.getMessage();
                        if ("Socket closed".equalsIgnoreCase(message)) {// 取消请求

                        } else {
                            callback.fail(exception.getMessage());
                        }

                    }
                });
    }
}
