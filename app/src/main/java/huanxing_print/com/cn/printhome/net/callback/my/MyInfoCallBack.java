package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public abstract class MyInfoCallBack implements BaseCallback{
    public abstract void success(String msg, MyInfoBean bean);
}
