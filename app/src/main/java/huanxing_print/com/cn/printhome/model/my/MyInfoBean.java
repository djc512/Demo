package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MyInfoBean {

    private String faceUrl;//头像
    private String memberId;
    private String mobileNumber;
    // private String monthConsume;//本月消费
    private String name;
    private String nickName;
    private String totleBalance;//余额
    private String totlePrintCount;//总打印量
    private String uniqueModifyFlag;//能否修改印家号
    private String wechatId;//微信id，有值说明已绑定微信
    private String wechatName;//微信昵称，有值说明已绑定微信

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

    public String getTotlePrintCount() {
        return totlePrintCount;
    }

    public void setTotlePrintCount(String totlePrintCount) {
        this.totlePrintCount = totlePrintCount;
    }

    public String getUniqueModifyFlag() {
        return uniqueModifyFlag;
    }

    public void setUniqueModifyFlag(String uniqueModifyFlag) {
        this.uniqueModifyFlag = uniqueModifyFlag;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }
}
