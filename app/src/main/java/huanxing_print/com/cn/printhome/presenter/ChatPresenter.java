package huanxing_print.com.cn.printhome.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * 作者： itheima
 * 时间：2016-10-18 17:19
 * 网址：http://www.itheima.com
 */

public interface ChatPresenter {

    void initChat(String contact);

    void updateData(String username);

    void sendMessage(String username, String msg);
    void sendGroupMessage(String username, EMMessage msg);
}
