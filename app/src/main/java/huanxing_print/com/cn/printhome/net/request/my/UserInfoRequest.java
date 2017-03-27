package huanxing_print.com.cn.printhome.net.request.my;

import android.content.Context;

import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.UserInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.my.UserInfoResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UserInfoRequest extends BaseRequst {
    /**
     * 获取用户信息
     */
    public static void getUserInfo(Context ctx, final UserInfoCallBack callback){
        String userInfoUrl = HTTP_URL+ HttpUrl.userInfo;

      HttpUtils.get(ctx, userInfoUrl, BaseApplication.getInstance().getLoginToken(), new HttpCallBack() {
          @Override
          public void success(String content) {
              UserInfoResolve resolve = new UserInfoResolve(content);
              resolve.resolve(callback);
          }

          @Override
          public void fail(String exception) {
            callback.connectFail();
          }
      });
    }
}
