package huanxing_print.com.cn.printhome.net.callback.contact;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by wanghao on 2017/5/9.
 */

public abstract class PhoneContactCallback implements BaseCallback{
    public abstract void success(String msg, ArrayList<FriendSearchInfo> searchInfos);
}
