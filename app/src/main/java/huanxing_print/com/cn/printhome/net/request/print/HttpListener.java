package huanxing_print.com.cn.printhome.net.request.print;


/**
 * Created by Liugh on 2016/11/28.
 */

public interface HttpListener<T> {

    public void onSucceed(String content);

    public void onFailed(String exception);
}