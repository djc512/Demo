package huanxing_print.com.cn.printhome.net.request.login;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.request.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.login.VeryCodeCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.login.VeryCodeResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class VeryCodeRequest extends BaseRequst {
    /**
     * 获取验证码
     * @param phoneNum
     */
    public static void getVeryCode(Context ctx, String type,String phoneNum, final VeryCodeCallback callback){
        String veryCodeUrl = HTTP_URL+ HttpUrl.getVeryCode;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobileNumber",phoneNum);
        params.put("type",type);

        HttpUtils.post(ctx, veryCodeUrl, "", params, new HttpCallBack() {
            @Override
            public void onSucceed(String content) {
                VeryCodeResolve resolve = new VeryCodeResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void onFailed(String exception) {
                callback.connectFail();
            }
        });
    }
}
