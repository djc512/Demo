package huanxing_print.com.cn.printhome.event.print;

/**
 * Created by LGH on 2017/5/25.
 */

public class PayTypeEvent {

    public static final int TYPE_PRINT = 1;//打印支付
    private int payType;

    @Override
    public String toString() {
        return "PayTypeEvent{" +
                "payType=" + payType +
                '}';
    }

    public PayTypeEvent(int payType) {
        this.payType = payType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
