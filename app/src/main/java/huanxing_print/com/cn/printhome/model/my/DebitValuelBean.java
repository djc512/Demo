package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitValuelBean {

    /**
     * data : 28567
     * errorCode : 23
     * errorMsg :
     * success : true
     */

    private int data;
    private int errorCode;
    private String errorMsg;
    private boolean success;

    public int getData() {
        return data;
    }

    public void setData(int data) {
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
