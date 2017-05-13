package huanxing_print.com.cn.printhome.net.request.approval;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.model.approval.AddApprovalObject;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.AddApprovalCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.ApprovalCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.CheckVoucherCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalListCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryLastCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryMessageCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.approval.AddApprovalResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.ApprovalResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.CheckVoucherResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.LastApprovalResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.QueryApprovalDetailResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.QueryApprovalListResolve;
import huanxing_print.com.cn.printhome.net.resolve.approval.QueryMessageResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * Created by dd on 2017/5/8.
 */

public class ApprovalRequest extends BaseRequst {

    /**
     * 查询审批列表
     *
     * @param context
     * @param pageNum 分页页码
     * @param type    1待我审批 2我已审批 3我发起的 4 抄送我的
     */
    public static void getQueryApprovalList(Context context, int pageNum, int pageSize, long type,
                                            String loginToken,
                                            final QueryApprovalListCallBack callBack) {
        Log.i("CMCC", "loginToken:" + loginToken);
        String url = HTTP_URL + HttpUrl.queryApprovalList;
        HttpUtils.getApprovalParam(context, url, loginToken, pageNum, pageSize, type, new HttpCallBack() {
            @Override
            public void success(String content) {
                QueryApprovalListResolve resolve = new QueryApprovalListResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }

    /**
     * 查询审批列表详情
     *
     * @param context
     * @param loginToken
     * @param approveId  审批单id
     * @param callBack
     */
    public static void getQueryApprovalDetail(Context context, String loginToken,
                                              String approveId, final QueryApprovalDetailCallBack callBack) {
        String url = HTTP_URL + HttpUrl.queryApprovalDetail;

        HttpUtils.getApprovalDetailParam(context, url, loginToken, approveId, new HttpCallBack() {
            @Override
            public void success(String content) {
                QueryApprovalDetailResolve resolve = new QueryApprovalDetailResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });

    }

    /**
     * 新增审批
     *
     * @param type 类型
     */
    public static void addApproval(Context context, String loginToken, int type,
                                   AddApprovalObject object, final AddApprovalCallBack addApprovalCallBack) {
        String url = HTTP_URL + HttpUrl.addApproval;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amountMonney", object.getAmountMonney());
        params.put("approverList", object.getApproverList());
        params.put("attachmentList", object.getAttachmentList());
        params.put("bankAccount", object.getBankAccount());
        params.put("bankName", object.getBankName());
        params.put("bankPerson", object.getBankPerson());
        params.put("copyerList", object.getCopyerList());
        params.put("department", object.getDepartment());
        params.put("finishTime", object.getFinishTime());
        params.put("purchaseList", object.getPurchaseList());
        params.put("remark", object.getRemark());
        params.put("subFormList", object.getSubFormList());
        params.put("title", object.getTitle());
        params.put("type", type);
        params.put("groupId", object.getGroupId());

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                AddApprovalResolve approvalResolve = new AddApprovalResolve(content);
                approvalResolve.resolve(addApprovalCallBack);
            }

            @Override
            public void fail(String exception) {
                addApprovalCallBack.connectFail();
            }
        });
    }


    /**
     * 查询上次审批人
     *
     * @param context
     * @param loginToken
     * @param type       1采购 2报销
     * @param callBack
     */
    public static void queryLast(Context context, String loginToken, int type,
                                 final QueryLastCallBack callBack) {
        String url = HTTP_URL + HttpUrl.queryLast;

        HttpUtils.getLastApprovalParam(context, url, loginToken, type, new HttpCallBack() {
            @Override
            public void success(String content) {
                LastApprovalResolve resolve = new LastApprovalResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }


    /**
     * 审批
     *
     * @param context
     * @param loginToken
     * @param approveId  审批单id
     * @param isPass     1通过 2 不通过
     * @param signUrl    签名地址
     * @param callBack
     */
    public static void approval(Context context, String loginToken, String approveId,
                                int isPass, String signUrl,
                                final ApprovalCallBack callBack) {
        String url = HTTP_URL + HttpUrl.approval;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("approveId", approveId);
        params.put("isPass", isPass);
        params.put("signUrl", signUrl);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                ApprovalResolve resolve = new ApprovalResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }

    /**
     * 查看凭证
     *
     * @param context
     * @param loginToken
     * @param approveId
     * @param type       0采购 1 报销
     * @param callBack
     */
    public static void checkVoucher(Context context, String loginToken, String approveId,
                                    int type, final CheckVoucherCallBack callBack) {
        String url = HTTP_URL + HttpUrl.proof;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("approveId", approveId);
        params.put("type", type);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
            @Override
            public void success(String content) {
                CheckVoucherResolve resolve = new CheckVoucherResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }


    /**
     * 查询未读消息
     *
     * @param context
     * @param loginToken
     * @param callBack
     */
    public static void queryUnreadMessage(Context context, String loginToken,
                                          final QueryMessageCallBack callBack) {
        String url = HTTP_URL + HttpUrl.queryCount;

        HttpUtils.getUnreadMessageCountParam(context, url, loginToken, new HttpCallBack() {
            @Override
            public void success(String content) {
                QueryMessageResolve resolve = new QueryMessageResolve(content);
                resolve.resolve(callBack);
            }

            @Override
            public void fail(String exception) {
                callBack.connectFail();
            }
        });
    }
}
