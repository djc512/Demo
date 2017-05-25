package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiRecordListDetailBean {

    /**
     * count : 10
     * list : [{"date":"2017年03月","detail":[{"amount":"0.01","context":"打印文件1份","date":"03月08日   00:29","type":0}]}]
     */
    private String amount;
    private String context;
    private String date;
    private int type;
    private String  orderId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
