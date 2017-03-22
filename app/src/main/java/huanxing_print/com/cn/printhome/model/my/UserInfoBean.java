package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UserInfoBean {
    //    addTime		string
//    faceUrl	头像	string
//    memberId	会员id	string
//    mobileNumber	手机号	string
//    name	名称	string
//    nickName	昵称	string
//    updateTime		string
//    wechatId	微信id，有值说明已绑定微信	string
//    errorCode		number
//    errorMsg		string
//    success		boolean

    private String addTime;
    private String faceUrl;
    private String memberId;
    private String mobileNumber;
    private String nickName;
    private String updateTime;
    private String wechatId;
    private int errorCode;
    private String errorMsg;
    private boolean success;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
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
