package huanxing_print.com.cn.printhome.net.callback.contact;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by wanghao on 2017/5/8.
 */

public abstract class MyFriendListCallback implements BaseCallback{
    public abstract void success(String msg, ArrayList<FriendInfo> friendInfos);
}
