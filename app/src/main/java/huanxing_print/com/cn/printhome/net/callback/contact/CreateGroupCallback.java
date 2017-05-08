package huanxing_print.com.cn.printhome.net.callback.contact;

import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by wanghao on 2017/5/8.
 */

public abstract class CreateGroupCallback implements BaseCallback{
    public abstract void success(String msg, GroupInfo groupInfo);
}
