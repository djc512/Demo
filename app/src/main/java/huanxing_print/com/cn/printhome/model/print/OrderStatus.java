package huanxing_print.com.cn.printhome.model.print;

/**
 * Created by LGH on 2017/3/24.
 */

public class OrderStatus {
    private boolean needAwake;
    private int waitingCount;

    public boolean isNeedAwake() {
        return needAwake;
    }

    public void setNeedAwake(boolean needAwake) {
        this.needAwake = needAwake;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }
}
