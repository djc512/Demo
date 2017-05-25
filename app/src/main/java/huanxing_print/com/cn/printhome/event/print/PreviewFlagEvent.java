package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/24.
 */

public class PreviewFlagEvent {

    private boolean previewFlag;

    public PreviewFlagEvent(boolean previewFlag) {
        this.previewFlag = previewFlag;
    }

    public boolean isPreviewFlag() {
        return previewFlag;
    }

    public void setPreviewFlag(boolean previewFlag) {
        this.previewFlag = previewFlag;
    }
}
