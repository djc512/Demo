package huanxing_print.com.cn.printhome.net.request.print;

import android.app.Activity;
import android.content.DialogInterface;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Map;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.time.TimeUtils;
import huanxing_print.com.cn.printhome.view.dialog.WaitDialog;
import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by LGH on 2017/3/20.
 */

public class Http {

    public static void postString(final Activity activity, final String url, Map<String, Object> params, Map<String,
            String>
            headerMap, final HttpListener callback) {
        String paramsStr = new GsonBuilder().serializeNulls().create().toJson(params);
        Logger.d("http-request:" + url + "----" + paramsStr);
        TimeUtils.beginTime();

        final RequestCall requestCall = OkHttpUtils.postString()
                .url(url)
                .headers(headerMap)
                .content(paramsStr)
                .tag(activity)
                .build();
        requestCall.execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                WaitDialog.dismissDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                WaitDialog.showDialog(activity, requestCall, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        requestCall.cancel();
                    }
                });
            }

            @Override
            public void onResponse(String result, int arg1) {
                TimeUtils.endTime();
                Logger.d("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
//                        callback.onSucceed(result);
            }

            @Override
            public void onError(Call call, Exception exception, int arg2) {
                TimeUtils.endTime();
                Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
                String message = exception.getMessage();
                if ("Socket closed".equalsIgnoreCase(message)) {
                } else {
//                            callback.onFailed(exception.getMessage());
                }
            }
        });
    }
}
