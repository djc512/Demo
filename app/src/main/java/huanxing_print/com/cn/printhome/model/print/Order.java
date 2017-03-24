package huanxing_print.com.cn.printhome.model.print;

/**
 * Created by LGH on 2017/3/23.
 */

public class Order {
    private String orderId;
    private String totalAmount;
    private String totleBalance;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotleBalance() {
        return totleBalance;
    }

    public void setTotleBalance(String totleBalance) {
        this.totleBalance = totleBalance;
    }
}
