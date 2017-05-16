package huanxing_print.com.cn.printhome.net.callback.chat;

import huanxing_print.com.cn.printhome.model.chat.CommonPackage;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/8.
 */

public interface GetCommonPackageDetailCallBack extends BaseCallback {
    void success(String msg, CommonPackage detail);
}
