package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiBean {

    /**
     * rechargeAmout : 1000.00
     * sendAmount : 500.00
     */
    private String blackPrice;//黑白价钱
    private String colorPrice;//彩色价钱
    private String rechargeAmout;
    private String sendAmount;

    public String getBlackPrice() {
        return blackPrice;
    }

    public void setBlackPrice(String blackPrice) {
        this.blackPrice = blackPrice;
    }

    public String getColorPrice() {
        return colorPrice;
    }

    public void setColorPrice(String colorPrice) {
        this.colorPrice = colorPrice;
    }

    public String getRechargeAmout() {
        return rechargeAmout;
    }

    public void setRechargeAmout(String rechargeAmout) {
        this.rechargeAmout = rechargeAmout;
    }

    public String getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(String sendAmount) {
        this.sendAmount = sendAmount;
    }
}
