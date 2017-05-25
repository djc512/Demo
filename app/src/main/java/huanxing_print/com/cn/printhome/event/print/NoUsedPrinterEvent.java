package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/25.
 */

public class NoUsedPrinterEvent {
    private boolean flag;

    public NoUsedPrinterEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
