package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoBean {

    private String faceUrl;//头像
    private int memberId;
    private String mobileNumber;
    private String monthConsume;//本月消费
    private String name;
    private String nickName;
    private String totleBalance;//余额

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
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

    @Override
    public String toString() {
        return "MyInfoBean{" +
                "faceUrl='" + faceUrl + '\'' +
                ", memberId=" + memberId +
                ", mobileNumber=" + mobileNumber +
                ", monthConsume='" + monthConsume + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", totleBalance='" + totleBalance + '\'' +
                '}';
    }
}
