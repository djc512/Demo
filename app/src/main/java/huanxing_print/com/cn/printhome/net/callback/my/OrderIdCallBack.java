package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public abstract class OrderIdCallBack implements BaseCallback {
    public abstract void success(String msg, String data);
}
