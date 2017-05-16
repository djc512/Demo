package huanxing_print.com.cn.printhome.net.request.contact;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.CreateGroupCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.NullResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.CreateGroupResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.GroupListResolve;
import huanxing_print.com.cn.printhome.net.resolve.contact.GroupMessageResolve;
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

    /**
     * 群信息查询
     * @param ctx
     * @param logintoken
     * @param groupId
     * @param callback
     */
    public static void queryGroupMessage(Context ctx, String logintoken, String groupId, String easemobGroupId, final GroupMessageCallback callback) {
        String queryGroupMsgUrl = HTTP_URL + HttpUrl.queryGroupMsg;

        HttpUtils.getGroupMsgParam(ctx, queryGroupMsgUrl, logintoken, groupId, easemobGroupId, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "getGroupMsgParam" + content);
                GroupMessageResolve resolve = new GroupMessageResolve(content);
                resolve.resolve(callback);
            }

            @Override
            public void fail(String exception) {
                callback.connectFail();
            }
        });
    }

    /**
     * 添加群成员
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void addMemberToGroup(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String addMemberUrl = HTTP_URL + HttpUrl.addMemberToGroup;

        HttpUtils.post(ctx, addMemberUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "addMemberToGroup" + content);
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
     * 删除群成员
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void delMemberFromGroup(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String addMemberUrl = HTTP_URL + HttpUrl.delMemberFromGroup;

        HttpUtils.post(ctx, addMemberUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "delMemberFromGroup" + content);
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
     * 退出群
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void exitGroup(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String exitGroupUrl = HTTP_URL + HttpUrl.exitGroup;

        HttpUtils.post(ctx, exitGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "exitGroup" + content);
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
     * 群转让
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void transfer(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String exitGroupUrl = HTTP_URL + HttpUrl.transferGroup;

        HttpUtils.post(ctx, exitGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "exitGroup" + content);
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
     * 解散群
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void dissolution(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String exitGroupUrl = HTTP_URL + HttpUrl.dissolutionGroup;

        HttpUtils.post(ctx, exitGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "dissolutionGroup" + content);
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
     * 修改群
     * @param ctx
     * @param logintoken
     * @param params
     * @param callback
     */
    public static void modify(Context ctx, String logintoken, Map<String, Object> params, final NullCallback callback) {
        String exitGroupUrl = HTTP_URL + HttpUrl.modifyGroup;

        HttpUtils.post(ctx, exitGroupUrl, logintoken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.e("wanghao", "modifyGroup" + content);
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
