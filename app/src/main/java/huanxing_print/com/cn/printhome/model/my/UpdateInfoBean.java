package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by DjC512 on 2017-3-25.
 */

public class UpdateInfoBean {

    /**
     * data :
     * errorCode : 0
     * errorMsg :
     * success : true
     */

    private String data;
    private int errorCode;
    private String errorMsg;
    private boolean success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
