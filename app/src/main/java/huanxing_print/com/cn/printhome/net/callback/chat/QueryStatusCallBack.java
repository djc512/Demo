package huanxing_print.com.cn.printhome.net.callback.chat;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.chat.Msg;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by dd on 2017/5/8.
 */

public interface QueryStatusCallBack extends BaseCallback {
    void success(String msg, ArrayList<Msg> data);
}
