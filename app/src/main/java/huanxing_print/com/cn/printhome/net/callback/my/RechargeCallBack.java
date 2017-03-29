package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.RechargeBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public abstract class RechargeCallBack implements BaseCallback {
    public abstract void success(String msg, RechargeBean bean);
}
