package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public abstract class DebitValueCallBack implements BaseCallback {
    public abstract void success(String msg, String bean);
}
