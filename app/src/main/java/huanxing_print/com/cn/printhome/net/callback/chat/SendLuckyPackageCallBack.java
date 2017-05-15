package huanxing_print.com.cn.printhome.net.callback.chat;

import huanxing_print.com.cn.printhome.model.chat.LuckyPackage;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/8.
 */

public interface SendLuckyPackageCallBack extends BaseCallback {
    void success(String msg, LuckyPackage luckyPackage);
}
