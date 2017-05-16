package huanxing_print.com.cn.printhome.net.callback.my;

import java.util.List;

import huanxing_print.com.cn.printhome.model.my.OrderListBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public abstract class OrderListCallBack implements BaseCallback {
    public abstract void success(List<OrderListBean> bean);
}
