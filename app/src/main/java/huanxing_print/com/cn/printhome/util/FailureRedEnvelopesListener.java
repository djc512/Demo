package huanxing_print.com.cn.printhome.util;

/**
 * 失效红包的回调
 * Created by dd on 2017/5/10.
 */

public interface FailureRedEnvelopesListener {
    void checkDetail();

    void closeDialog();
}
