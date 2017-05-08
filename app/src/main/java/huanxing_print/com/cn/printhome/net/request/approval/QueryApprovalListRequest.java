package huanxing_print.com.cn.printhome.net.request.approval;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalListCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;
import huanxing_print.com.cn.printhome.net.resolve.approval.QueryApprovalListResolve;
import huanxing_print.com.cn.printhome.util.HttpUtils;

/**
 * 查询审批列表
 * Created by dd on 2017/5/8.
 */

public class QueryApprovalListRequest extends BaseRequst {

    /**
     * @param context
     * @param pageNum  分页页码
     * @param pageSize 分页大小
     * @param type     1待我审批 2我已审批 3我发起的 4 抄送我的
     */
    public static void getQueryApprovalList(Context context, String loginToken, long pageNum,
                                            long pageSize, long type,
                                            final QueryApprovalListCallBack callBack) {
        String url = HTTP_URL + HttpUrl.queryApprovalList;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("type", type);

        HttpUtils.post(context, url, loginToken, params, new HttpCallBack() {
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
}
