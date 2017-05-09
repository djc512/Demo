package huanxing_print.com.cn.printhome.net.request.contact;

import android.content.Context;

import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.contact.CreateGroupCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.CreateGroupResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.GroupListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by wanghao on 2017/5/8.
 */

public class GroupManagerRequest extends BaseRequst{
    /**
     * 查询群列表
     * @param ctx
     * @param logintoken
     * @param callback
     */
    public static void queryGroupList(Context ctx, String logintoken, final GroupListCallback callback) {
        String groupListUrl = HTTP_URL + HttpUrl.groupList;

        HttpUtils.get(ctx, groupListUrl, logintoken, new HttpCallBack() {
            @Override
            public void success(String content) {
                GroupListResolve resolve = new GroupListResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }

    /**
     * 添加群
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void createGroupReq(Context ctx, String logintoken, Map<String, Object> params, final CreateGroupCallback callback) {
        String createGroupUrl = HTTP_URL + HttpUrl.createGroup;

        HttpUtils.post(ctx, createGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                CreateGroupResolve resolve = new CreateGroupResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }
}