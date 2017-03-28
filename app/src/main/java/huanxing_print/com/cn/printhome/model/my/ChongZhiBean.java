package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiBean {

    /**
     * data : [{"rechargeAmout":"测试内容s611","sendAmount":"测试内容o816"}]
     * errorCode : 0
     * errorMsg :
     * success : true
     */

    private int errorCode;
    private String errorMsg;
    private boolean success;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rechargeAmout : 测试内容s611
         * sendAmount : 测试内容o816
         */

        private String rechargeAmout;
        private String sendAmount;

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
}
