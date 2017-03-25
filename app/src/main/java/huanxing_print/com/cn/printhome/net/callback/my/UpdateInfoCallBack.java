package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.UpdateInfoBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by DjC512 on 2017-3-25.
 */

public abstract class UpdateInfoCallBack implements BaseCallback {
    public abstract void success(String msg, UpdateInfoBean bean);
}
