package huanxing_print.com.cn.printhome.net.callback.my;

import huanxing_print.com.cn.printhome.model.my.UserInfoBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public abstract class UserInfoCallBack implements BaseCallback {
    public abstract void success(String msg, UserInfoBean bean);
}
