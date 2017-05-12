package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public abstract class WeChatCallBack implements BaseCallback {
    public abstract void success(WeChatPayBean bean);
}
