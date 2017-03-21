package huanxing_print.com.cn.printhome.model;

/**
 * Created by Liugh on 2016/11/30.
 */

public class CommonResp {

    private int errorCode;
    private String errorMsg;
    private int success;

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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
