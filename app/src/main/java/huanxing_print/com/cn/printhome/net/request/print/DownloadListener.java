package huanxing_print.com.cn.printhome.net.request.print;

/**
 * Created by LGH on 2017/5/25.
 */

public interface DownloadListener {
    public void onSucceed(String content);

    public void onFailed(String exception);
}
