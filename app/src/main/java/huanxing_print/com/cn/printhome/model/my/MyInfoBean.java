package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoBean {
    //    faceUrl	头像	number
//    memberId		number
//    mobileNumber		number
//    monthConsume	本月消费	string
//    name		number
//    nickName		number
//    totleBalance	余额	string
//    success		number
    private String faceUrl;
    private int memberId;
    private int mobileNumber;

    private String monthConsume;
    private String name;
    private String nickName;
    private String totleBalance;
    private boolean success;

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMonthConsume() {
        return monthConsume;
    }

    public void setMonthConsume(String monthConsume) {
        this.monthConsume = monthConsume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTotleBalance() {
        return totleBalance;
    }

    public void setTotleBalance(String totleBalance) {
        this.totleBalance = totleBalance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
