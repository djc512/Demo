package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public abstract class ChongzhiCallBack implements BaseCallback{
    public abstract void success(String msg,ChongZhiBean bean);
}
