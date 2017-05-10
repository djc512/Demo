package huanxing_print.com.cn.printhome.net.callback.contact;

import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by wanghao on 2017/5/10.
 */

public abstract class GroupMessageCallback implements BaseCallback{
    public abstract void success(String msg, GroupMessageInfo groupMessageInfo);
}
