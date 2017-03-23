package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public abstract class ChongZhiRecordCallBack implements BaseCallback {
    public abstract void success(String msg,ChongZhiRecordBean bean);
}
