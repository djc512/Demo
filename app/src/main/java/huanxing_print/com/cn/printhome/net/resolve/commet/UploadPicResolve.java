package huanxing_print.com.cn.printhome.net.resolve.commet;

import java.util.List;

import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.resolve.BaseResolve;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class UploadPicResolve extends BaseResolve<List<PicDataBean>> {

    public UploadPicResolve(String result) {
        super(result);
    }

    public void resolve(UpLoadPicCallBack callback) {
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
