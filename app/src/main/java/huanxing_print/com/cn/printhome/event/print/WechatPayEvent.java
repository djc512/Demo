package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/19.
 */

public class WechatPayEvent {

    private boolean result;

    @Override
    public String toString() {
        return "WechatPayEvent{" +
                "result=" + result +
                '}';
    }

    public WechatPayEvent(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
