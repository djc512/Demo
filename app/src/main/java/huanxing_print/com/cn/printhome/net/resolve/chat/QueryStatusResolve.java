package huanxing_print.com.cn.printhome.net.resolve.chat;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.chat.Msg;
import huanxing_print.com.cn.printhome.net.callback.chat.QueryStatusCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by dd on 2017/5/8.
 */

public class QueryStatusResolve extends BaseResolve<ArrayList<Msg>> {
    public QueryStatusResolve(String result) {
        super(result);
    }

    public void resolve(QueryStatusCallBack callback) {
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
