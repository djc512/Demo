package huanxing_print.com.cn.printhome.net.resolve.chat;

import huanxing_print.com.cn.printhome.model.chat.MemberHead;
import huanxing_print.com.cn.printhome.net.callback.chat.GetMemberHeadlCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class GetMemberHeadResolve extends BaseResolve<MemberHead> {
    public GetMemberHeadResolve(String result) {
        super(result);
    }

    public void resolve(GetMemberHeadlCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(successMsg, bean);
                break;
            case FAIL_CODE:
                callback.fail(errorMsg);
                break;
            default:
                callback.fail(errorMsg);
                break;
        }

    }
}
