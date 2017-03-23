package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiBean {
    //    rechargeAmout	充值金额	string
//    sendAmount	送的金额	string
//    errorCode		number
//    errorMsg		string
//    success

    private String rechargeAmout;
    private String sendAmount;
    private int errorCode;
    private String errorMsg;
    private boolean success;

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
