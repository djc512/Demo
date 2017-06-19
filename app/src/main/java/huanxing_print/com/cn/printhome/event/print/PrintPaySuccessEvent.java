package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/23.
 */

public class PrintPaySuccessEvent {
    private boolean finishFlag;

    public PrintPaySuccessEvent(boolean finishFlag) {
        this.finishFlag = finishFlag;
    }

    public boolean isFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(boolean finishFlag) {
        this.finishFlag = finishFlag;
    }
}
