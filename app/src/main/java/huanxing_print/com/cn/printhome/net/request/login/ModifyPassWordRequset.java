package huanxing_print.com.cn.printhome.net.request.login;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.request.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.login.ModifyPasswordCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.login.ModifyPasswordResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class ModifyPassWordRequset extends BaseRequst {
    /**
     * 修改密码
     * @param ctx
     * @param phoneNum
     * @param callback
     */
    public static  void modifyPwd(Context ctx, String validCode, String pwd, String phoneNum, final ModifyPasswordCallback callback){

        String modifyPwdUrl = HTTP_URL+ HttpUrl.resetPasswd;
        Map<String,Object> params = new HashMap<>();
        params.put("newPwd",pwd);
        params.put("mobileNumber",phoneNum);
        params.put("validCode",validCode);

        HttpUtils.post(ctx, modifyPwdUrl, "", params, new HttpCallBack() {
            @Override
            public void onSucceed(String content) {
                ModifyPasswordResolve resolve = new ModifyPasswordResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void onFailed(String exception) {
                callback.connectFail();
            }
        });
    }
}
