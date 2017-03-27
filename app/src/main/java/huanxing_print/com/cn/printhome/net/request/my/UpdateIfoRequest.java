package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.UpdateInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.UpdateInfoResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by DjC512 on 2017-3-25.
 */

public class UpdateIfoRequest extends BaseRequst {

//    faceUrl	头像地址	string
//    mobileNumber	手机号	string
//    nickName	名字	string
//    password	新密码	string
//    sex	性别	string
//    validCode	验证码，修改手机号时必填	string

    public static void updateInfo(Context ctx, String url,
                                  String num,
                                  String nickName,
                                  String password,
                                  String sex,
                                  String validCode,
                                  final UpdateInfoCallBack callBack) {
        String updateInfoUrl = HTTP_URL + HttpUrl.updateInfo;

        Map<String, Object> params = new HashMap<>();

        params.put("faceUrl", url);
        params.put("nickName", nickName);
        params.put("mobileNumber", num);
        params.put("password", password);
        params.put("sex", sex);
        params.put("validCode", validCode);

        HttpUtils.post(ctx, updateInfoUrl, BaseApplication.getInstance().getLoginToken(), params, new HttpCallBack() {
            @Override
            public void success(String content) {
                UpdateInfoResolve resolve = new UpdateInfoResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }
}
