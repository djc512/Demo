package huanxing_print.com.cn.printhome.model.chat;

/**
 * Created by dd on 2017/5/15.
 */

public class RedPackageDetail {
    private String amount;
    private String faceUrl;
    private String invalid;
    private String masterFaceUrl;
    private String masterName;
    private String name;
    private String remark;
    private String snatch;
    private String time;
    private String receiveMemberId;//发给谁的
    private String sendMemberId;//谁发的

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }


    public String getMasterFaceUrl() {
        return masterFaceUrl;
    }

    public void setMasterFaceUrl(String masterFaceUrl) {
        this.masterFaceUrl = masterFaceUrl;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public String getSnatch() {
        return snatch;
    }

    public void setSnatch(String snatch) {
        this.snatch = snatch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiveMemberId() {
        return receiveMemberId;
    }

    public void setReceiveMemberId(String receiveMemberId) {
        this.receiveMemberId = receiveMemberId;
    }

    public String getSendMemberId() {
        return sendMemberId;
    }

    public void setSendMemberId(String sendMemberId) {
        this.sendMemberId = sendMemberId;
    }
}
