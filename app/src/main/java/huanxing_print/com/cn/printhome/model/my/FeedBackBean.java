package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class FeedBackBean {
    //    errorCode		number
//    errorMsg		string
//    success
    private int errorCode;
    private String errorMsg;
    private boolean success;

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
