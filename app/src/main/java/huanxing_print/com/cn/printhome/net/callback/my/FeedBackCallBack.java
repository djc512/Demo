package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.FeedBackBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public abstract class FeedBackCallBack implements BaseCallback {
    public abstract void success(String msg, FeedBackBean bean);
}
