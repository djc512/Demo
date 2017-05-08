package huanxing_print.com.cn.printhome.net.request.contact;

import android.content.Context;

import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.FriendSearchCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.NewFriendCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.FriendSearchResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.MyFriendListResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.NewFriendResolve;
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

    /**
     * 印家号／手机号搜索
     * @param ctx
     * @param logintoken
     * @param searchName
     * @param callback
     */
    public static void friendSearch(Context ctx, String logintoken,String searchName, final FriendSearchCallback callback) {
        String friendSearchUrl = HTTP_URL + HttpUrl.friendSearch;

        HttpUtils.getFriendSearchParam(ctx, friendSearchUrl, logintoken, searchName, new HttpCallBack() {
            @Override
            public void success(String content) {
                FriendSearchResolve resolve = new FriendSearchResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }

    /**
     * 添加好友请求
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void searchAddFriend(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String friendSearchUrl = HTTP_URL + HttpUrl.friendSearchAdd;

        HttpUtils.post(ctx, friendSearchUrl, logintoken, params, new HttpCallBack() {
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

    /**
     * 新的朋友列表
     * @param ctx
     * @param logintoken
     * @param callback
     */
    public static void queryNewFriendList(Context ctx, String logintoken, final NewFriendCallback callback) {
        String newFriendUrl = HTTP_URL + HttpUrl.newFriend;

        HttpUtils.get(ctx, newFriendUrl, logintoken, new HttpCallBack() {
            @Override
            public void success(String content) {
                NewFriendResolve resolve = new NewFriendResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }

    /**
     * 处理加好友请求
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void operationNewFriend(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String operationUrl = HTTP_URL + HttpUrl.operationNewFriend;

        HttpUtils.post(ctx, operationUrl, logintoken, params, new HttpCallBack() {
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
