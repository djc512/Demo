package huanxing_print.com.cn.printhome.net.resolve.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.print.ADInfo;
import huanxing_print.com.cn.printhome.net.callback.my.ADCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class ADListResolve extends BaseResolve<List<ADInfo>> {
    public ADListResolve(String result) {
        super(result);
    }

    public void resolve(ADCallBack callback) {
        switch (code) {
            case SUCCESS_CODE:
                callback.success(bean);
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
