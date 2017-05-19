package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/19.
 */

public class PrintTypeEvent {

    public static final int TYPE_PRINT = 1;
    public static final int TYPE_COPY = 2;

    private Integer type;

    public PrintTypeEvent(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PrintTypeEvent{" +
                "type=" + type +
                '}';
    }
}
