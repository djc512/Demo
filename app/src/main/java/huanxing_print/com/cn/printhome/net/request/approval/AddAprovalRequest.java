package huanxing_print.com.cn.printhome.net.request.approval;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.AddApprovalCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.approval.AddApprovalResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * 新增审批
 * Created by dd on 2017/5/8.
 */

public class AddAprovalRequest extends BaseRequst {

    /**
     * 新增审批
     *
     * @param amountMonney   总金额
     * @param approverList   审批人列表
     * @param attachmentList 附件列表
     * @param bankAccount    收款方账号
     * @param bankName       收款方开户行
     * @param bankPerson     收款方全称
     * @param copyerList     抄送人列表
     * @param department     部门
     * @param finishTime     完成时间
     * @param purchaseList   采购清单
     * @param remark         备注
     * @param subFormList    报销条目
     * @param title          标题
     * @param type           类型
     */
    public static void addApproval(Context context, String loginToken, String amountMonney, ArrayList<Object> approverList,
                                   ArrayList<Object> attachmentList, String bankAccount,
                                   String bankName, String bankPerson, ArrayList<Object> copyerList,
                                   String department, String finishTime, String purchaseList,
                                   String remark, ArrayList<Object> subFormList, String title,
                                   long type, final AddApprovalCallBack addApprovalCallBack) {
        String url = HTTP_URL + HttpUrl.addApproval;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amountMonney", amountMonney);
        params.put("approverList", approverList);
        params.put("attachmentList", attachmentList);
        params.put("bankAccount", bankAccount);
        params.put("bankName", bankName);
        params.put("bankPerson", bankPerson);
        params.put("copyerList", copyerList);
        params.put("department", department);
        params.put("finishTime", finishTime);
        params.put("purchaseList", purchaseList);
        params.put("remark", remark);
        params.put("subFormList", subFormList);
        params.put("title", title);
        params.put("type", type);

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
}
