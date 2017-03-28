package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.OrderDetailBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public abstract class OrderDetailCallBack implements BaseCallback {
    public abstract void success(String msg, OrderDetailBean bean);
}
