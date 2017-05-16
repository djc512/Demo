package huanxing_print.com.cn.printhome.presenter;

/**
 * 作者： itheima
 * 时间：2016-10-18 17:19
 * 网址：http://www.itheima.com
 */

public interface ChatPresenter {

    void initChat(String contact,int kind);

    void updateData(String username,int kind);

    void sendMessage(String username, String msg,int type,int kind);
    void sendImgMessage(String username, String url,int type,int kind);
}
