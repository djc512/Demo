package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public abstract class DaYinListCallBack implements BaseCallback {
    public abstract void success(String msg, DaYinListBean bean);
}
