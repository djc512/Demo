package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.TotleBalanceBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public abstract class TotleBalanceCallBack implements BaseCallback {
    public abstract void success(String msg, TotleBalanceBean bean);

}
