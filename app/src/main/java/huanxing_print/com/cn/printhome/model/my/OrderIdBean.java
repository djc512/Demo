package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class OrderIdBean {

    /**
     * data : 测试内容m78y
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
