package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class WeChatPayBean {
//    data : {"appId":"wxb54a2ee8a63993f9","nonceStr":"xcmjntnlk19nn1he","partnerId":"1457268302","paySign":"5B28851D6ACC6C2A9E656A597769CBF9","pkg":"Sign=WXPay","prepayId":"wx20170512171348b744b70dc1016436732","timeStamp":"1494580428"}
    private String appId;
    private String nonceStr;
    private String partnerId;
    private String paySign;
    private String pkg;
    private String prepayId;
    private String timeStamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
