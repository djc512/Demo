package huanxing_print.com.cn.printhome.net.request.print;

import android.app.Activity;
import android.content.DialogInterface;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.util.time.TimeUtils;
import huanxing_print.com.cn.printhome.view.dialog.WaitDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by LGH on 2017/3/20.
 */

public class Http {

    public static void postString(final Activity activity, final String url, Map<String, Object> params, Map<String,
            String> headerMap, final HttpListener callback, final boolean showDialog) {
        String paramsStr = new GsonBuilder().serializeNulls().create().toJson(params);
        Logger.i("http-request:" + url + "----" + paramsStr);
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
                Logger.i(url);
                if (showDialog) {
                    WaitDialog.dismissDialog();
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                Logger.i(url);
                if (showDialog) {
                    WaitDialog.showDialog(activity, requestCall, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            requestCall.cancel();
                        }
                    });
                }
            }

            @Override
            public void onResponse(String result, int arg1) {
                TimeUtils.endTime();
                Logger.i("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
                callback.onSucceed(result);
            }

            @Override
            public void onError(Call call, Exception exception, int arg2) {
                TimeUtils.endTime();
                Logger.i(url);
                Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime() + " ms");
                String message = exception.getMessage();
                if ("Socket closed".equalsIgnoreCase(message)) {
                } else {
                    callback.onFailed(exception.getMessage());
                }
            }
        });
    }

    public static void postFile(final Activity activity, final String url, final Map<String, Object> params, final
    Map<String, String> headerMap, final HttpListener callback, final boolean showDialog) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final String paramsStr = new GsonBuilder().serializeNulls().create().toJson(params);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.i("http-request:" + url + "----" + paramsStr);
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
                                Logger.i(url);
                                if (showDialog) {
                                    WaitDialog.dismissDialog();
                                }
                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                                Logger.i(url);
                                if (showDialog) {
                                    WaitDialog.showDialog(activity, requestCall, new DialogInterface.OnCancelListener
                                            () {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            requestCall.cancel();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onResponse(String result, int arg1) {
                                TimeUtils.endTime();
                                Logger.i("http-result:" + url + "----" + result + "----" + TimeUtils.subTime() + " ms");
                                callback.onSucceed(result);
                            }

                            @Override
                            public void onError(Call call, Exception exception, int arg2) {
                                TimeUtils.endTime();
                                Logger.i(url);
                                Logger.e("http-exception:" + url + "----" + exception + "----" + TimeUtils.subTime()
                                        + " ms");
                                String message = exception.getMessage();
                                if ("Socket closed".equalsIgnoreCase(message)) {
                                } else {
                                    callback.onFailed(exception.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }

    public static final void get(final Activity activity, final String url, Map<String, Object> params, Map<String,
            String> headerMap, final HttpListener callback, final boolean showDilog) {
        TimeUtils.beginTime();
        final String getUrl = UrlUtil.getUrl(url, params);
        final RequestCall requestCall = OkHttpUtils.get()
                .url(getUrl)
                .headers(headerMap)
                .tag(activity)
                .build();
        requestCall.execute(new StringCallback() {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                if (showDilog) {
                    WaitDialog.dismissDialog();
                }
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                if (showDilog) {
                    WaitDialog.showDialog(activity, requestCall, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            requestCall.cancel();
                        }
                    });
                }
            }

            @Override
            public void onResponse(String result, int arg1) {
                TimeUtils.endTime();
                Logger.i("http-result:" + getUrl + "----" + result + "----" + TimeUtils.subTime() + " ms");
                callback.onSucceed(result);
            }

            @Override
            public void onError(Call call, Exception exception, int arg2) {
                TimeUtils.endTime();
                Logger.i("http-exception:" + getUrl + "----" + exception + "----" + TimeUtils.subTime() + " ms");
                String message = exception.getMessage();
                if ("Socket closed".equalsIgnoreCase(message)) {

                } else {
                    callback.onFailed(exception.getMessage());
                }
            }
        });
    }

    public static final void download() {
        Request request = new Request.Builder().url("http://gdown.baidu" +
                ".com/data/wisegame/0852f6d39ee2e213/QQ_676.apk").build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    FileUtils.makeFile(FileUtils.getDownloadPath() + "asp.apk");
                    File file = new File(FileUtils.getDownloadPath() + "asp.apk");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Logger.i(progress);
                    }
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
