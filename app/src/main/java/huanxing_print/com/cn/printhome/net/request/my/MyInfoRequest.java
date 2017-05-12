package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.my.MyInfoResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoRequest extends BaseRequst {

    public static void getMyInfo(Context ctx,String logintoken, final MyInfoCallBack callBack) {
        String myInfoUrl = HTTP_URL + HttpUrl.myinfo;

        HttpUtils.get(ctx, myInfoUrl,logintoken , new HttpCallBack() {
            @Override
            public void success(String content) {
                MyInfoResolve resolve = new MyInfoResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }

    public static void bindWechat(Context context,String logintoken, String city, String country,
                                   String headimgurl, String nickName,String openId,
                                   String privilege, String sex, String unionId,
                                   final NullCallback callback) {

        String url = HTTP_URL + HttpUrl.bindWechat;
        // password = MD5Util.MD5(password);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("city", city);
        params.put("country", country);
        params.put("headimgurl", headimgurl);
        params.put("nickName", nickName);
        params.put("openId", openId);
        params.put("privilege", privilege);
        params.put("sex", sex);
        params.put("unionId", unionId);

        HttpUtils.post(context, url, logintoken, params, new HttpCallBack() {

            @Override
        public void success(String content) {
                NullResolve weiXinResolve = new NullResolve(content);
                weiXinResolve.resolve(callback);

            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }
}
