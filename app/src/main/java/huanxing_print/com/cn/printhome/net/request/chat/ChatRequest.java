package huanxing_print.com.cn.printhome.net.request.chat;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetCommonPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetGroupHeadlCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetLuckyPackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.GetMemberHeadlCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.PackageDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.QueryStatusCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.ReceivedPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.RobPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.SendCommonPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.SendLuckyPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.SendPackageCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.chat.GetCommonPackageDetailResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.GetGroupHeadResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.GetLuckyPackageDetailResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.GetMemberHeadResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.PackageDetailResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.QueryStatusResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.ReceivePackageResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.RobPackageResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.SendCommonPackageResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.SendLuckyPackageResolve;
import huanxing_print.com.cn.printhome.net.resolve.chat.SendPackageResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.util.time.TimeUtils;

/**
 * Created by dd on 2017/5/15.
 */

public class ChatRequest extends BaseRequst {

    /**
     * 查询应用消息提醒状态
     *
     * @param context
     * @param loginToken
     * @param msgIds
     * @param callBack
     */
    public static void queryMessageStatus(Context context, String loginToken, Map<String, Object> msgIds,
                                          final QueryStatusCallBack callBack) {
        String url = HTTP_URL + HttpUrl.queryMessageStatus;
        final String finalString = UrlUtil.getUrl(url, msgIds);
        HttpUtils.getQueryStatus(context, finalString, loginToken, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", "http-result:" + finalString + "----" + content + "----" + TimeUtils.subTime() + " ms");
                QueryStatusResolve resolve = new QueryStatusResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }

    /**
     * 发红包
     *
     * @param context
     * @param loginToken
     * @param amount
     * @param remark
     * @param transferMemberId
     * @param packageCallBack
     */
    public static void sendRedPackage(Context context, String loginToken,
                                      String amount, String remark, String transferMemberId,
                                      final SendPackageCallBack packageCallBack) {
        String url = HTTP_URL + HttpUrl.sendRedPackage;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", amount);
        params.put("remark", remark);
        params.put("transferMemberId", transferMemberId);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", content);
                SendPackageResolve approvalResolve = new SendPackageResolve(content);
                approvalResolve.resolve(packageCallBack);
            }

            @Override
            public void fail(String exception) {

                Log.d("CMCC", exception);
                packageCallBack.connectFail();
            }
        });
    }


    /**
     * 收红包
     *
     * @param context
     * @param loginToken
     * @param packetId
     * @param callBack
     */
    public static void receivePackage(Context context, String loginToken,
                                      String packetId, final ReceivedPackageCallBack callBack) {
        String url = HTTP_URL + HttpUrl.receiveRedPackage;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("packetId", packetId);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", content);
                ReceivePackageResolve approvalResolve = new ReceivePackageResolve(content);
                approvalResolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {

                Log.d("CMCC", exception);
                callBack.connectFail();
            }
        });
    }

    /**
     * 查询红包记录
     *
     * @param context
     * @param loginToken
     * @param packetId
     * @param callBack
     */
    public static void queryPackageDetail(Context context, String loginToken,
                                          String packetId,
                                          final PackageDetailCallBack callBack) {
        final String url = HTTP_URL + HttpUrl.queryPackageDetail;
        HttpUtils.queryPackageDetail(context, url, loginToken, packetId, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", "http-result:" + url + "----" + content + "----" + TimeUtils.subTime() + " ms");
                PackageDetailResolve resolve = new PackageDetailResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }

    /**
     * 发普通群红包
     *
     * @param context
     * @param loginToken
     * @param amount
     * @param easemobGroupId
     * @param groupId
     * @param remark
     * @param callBack
     */
    public static void sendCommonPackage(Context context, String loginToken,
                                         String amount, String easemobGroupId,
                                         String groupId, String remark,
                                         final SendCommonPackageCallBack callBack) {
        String url = HTTP_URL + HttpUrl.sendGroupPackage;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", amount);
        params.put("easemobGroupId", easemobGroupId);
        params.put("groupId", groupId);
        params.put("remark", remark);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", content);
                SendCommonPackageResolve approvalResolve = new SendCommonPackageResolve(content);
                approvalResolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {

                Log.d("CMCC", exception);
                callBack.connectFail();
            }
        });
    }

    /**
     * 发拼手气群红包
     *
     * @param context
     * @param loginToken
     * @param amount
     * @param easemobGroupId
     * @param groupId
     * @param remark
     * @param callBack
     */
    public static void sendLuckyPackage(Context context, String loginToken,
                                        String amount, String easemobGroupId,
                                        String groupId, int number, String remark,
                                        final SendLuckyPackageCallBack callBack) {
        String url = HTTP_URL + HttpUrl.sendLuckyGroupPackage;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", amount);
        params.put("easemobGroupId", easemobGroupId);
        params.put("groupId", groupId);
        params.put("number", number);
        params.put("remark", remark);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", content);
                SendLuckyPackageResolve approvalResolve = new SendLuckyPackageResolve(content);
                approvalResolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {

                Log.d("CMCC", exception);
                callBack.connectFail();
            }
        });
    }


    /**
     * 抢红包
     *
     * @param context
     * @param loginToken
     * @param easemobGroupId
     * @param groupId
     * @param packetId
     * @param callBack
     */
    public static void robRedPackage(Context context, String loginToken,
                                     String easemobGroupId, String groupId,
                                     String packetId,
                                     final RobPackageCallBack callBack) {
        String url = HTTP_URL + HttpUrl.robRedPackage;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("easemobGroupId", easemobGroupId);
        params.put("groupId", groupId);
        params.put("packetId", packetId);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                Log.d("CMCC", content);
                RobPackageResolve approvalResolve = new RobPackageResolve(content);
                approvalResolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {

                Log.d("CMCC", exception);
                callBack.connectFail();
            }
        });
    }


    /**
     * 查看群手气红包记录
     *
     * @param context
     * @param loginToken
     * @param easemobGroupId
     * @param groupId
     * @param packetId
     * @param callBack
     */
    public static void getLuckyPackageDetail(Context context, String loginToken,
                                             String easemobGroupId, String groupId,
                                             String packetId,
                                             final GetLuckyPackageDetailCallBack callBack) {
        final String url = HTTP_URL + HttpUrl.checkLuckyGroupPackageDetail;
        HttpUtils.getLuckyPackageDetail(context, url, loginToken, easemobGroupId,
                groupId, packetId, new HttpCallBack() {
                    @Override
                    public void success(String content) {
                        Log.d("CMCC", "http-result:" + url + "----" + content + "----" + TimeUtils.subTime() + " ms");
                        GetLuckyPackageDetailResolve resolve = new GetLuckyPackageDetailResolve(content);
                        resolve.resolve(callBack);
                    }

                    @Override
                    public void fail(String exception) {
                        callBack.connectFail();
                    }
                });

    }


    /**
     * 查看群普通红包记录
     *
     * @param context
     * @param loginToken
     * @param easemobGroupId
     * @param groupId
     * @param packetId
     * @param callBack
     */
    public static void getCommonPackageDetail(Context context, String loginToken,
                                              String easemobGroupId, String groupId,
                                              String packetId,
                                              final GetCommonPackageDetailCallBack callBack) {
        final String url = HTTP_URL + HttpUrl.checkGroupPackageDetail;
        HttpUtils.getLuckyPackageDetail(context, url, loginToken, easemobGroupId,
                groupId, packetId, new HttpCallBack() {
                    @Override
                    public void success(String content) {
                        Log.d("CMCC", "http-result:" + url + "----" + content + "----" + TimeUtils.subTime() + " ms");
                        GetCommonPackageDetailResolve resolve = new GetCommonPackageDetailResolve(content);
                        resolve.resolve(callBack);
                    }

                    @Override
                    public void fail(String exception) {
                        callBack.connectFail();
                    }
                });
    }


    /**
     * 查询用户头像名称
     *
     * @param context
     * @param loginToken
     * @param memberId
     * @param callBack
     */
    public static void getMemberHead(Context context, String loginToken,
                                     String memberId,
                                     final GetMemberHeadlCallBack callBack) {
        final String url = HTTP_URL + HttpUrl.getMemberHead;
        HttpUtils.getMemberHead(context, url, loginToken, memberId,
                new HttpCallBack() {
                    @Override
                    public void success(String content) {
                        Log.d("CMCC", "http-result:" + url + "----" + content + "----" + TimeUtils.subTime() + " ms");
                        GetMemberHeadResolve resolve = new GetMemberHeadResolve(content);
                        resolve.resolve(callBack);
                    }

                    @Override
                    public void fail(String exception) {
                        callBack.connectFail();
                    }
                });
    }

    /**
     * 查询用群头像名称
     *
     * @param context
     * @param loginToken
     * @param groupId
     * @param callBack
     */
    public static void getGroupHead(Context context, String loginToken,
                                    String groupId,
                                    final GetGroupHeadlCallBack callBack) {
        final String url = HTTP_URL + HttpUrl.getGroupHead;
        HttpUtils.getGroupHead(context, url, loginToken, groupId,
                new HttpCallBack() {
                    @Override
                    public void success(String content) {
                        Log.d("CMCC", "http-result:" + url + "----" + content + "----" + TimeUtils.subTime() + " ms");
                        GetGroupHeadResolve resolve = new GetGroupHeadResolve(content);
                        resolve.resolve(callBack);
                    }

                    @Override
                    public void fail(String exception) {
                        callBack.connectFail();
                    }
                });
    }
}
