package huanxing_print.com.cn.printhome.net.request.contact;

import android.content.Context;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.contact.MyFriendListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by wanghao on 2017/5/8.
 */

public class FriendManagerRequest extends BaseRequst{
    /**
     * 好友列表
     * @param ctx
     * @param logintoken
     * @param callback
     */
    public static void queryFriendList(Context ctx, String logintoken, final MyFriendListCallback callback) {
        String myFriendListUrl = HTTP_URL + HttpUrl.queryFriendList;

        HttpUtils.get(ctx, myFriendListUrl, logintoken, new HttpCallBack() {
            @Override
            public void success(String content) {
                MyFriendListResolve resolve = new MyFriendListResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }
}
