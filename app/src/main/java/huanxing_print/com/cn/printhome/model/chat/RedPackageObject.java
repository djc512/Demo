package huanxing_print.com.cn.printhome.model.chat;

/**
 * 发红包返回的对象
 * Created by dd on 2017/5/18.
 */

public class RedPackageObject {
    private String amount;
    private boolean invalid;
    private String packetId;
    private String remark;
    private String sendMemberId;
    private String sendMemberName;
    private String sendMemberUrl;
    private boolean snatch;
    private String time;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendMemberId() {
        return sendMemberId;
    }

    public void setSendMemberId(String sendMemberId) {
        this.sendMemberId = sendMemberId;
    }

    public String getSendMemberName() {
        return sendMemberName;
    }

    public void setSendMemberName(String sendMemberName) {
        this.sendMemberName = sendMemberName;
    }

    public String getSendMemberUrl() {
        return sendMemberUrl;
    }

    public void setSendMemberUrl(String sendMemberUrl) {
        this.sendMemberUrl = sendMemberUrl;
    }

    public boolean isSnatch() {
        return snatch;
    }

    public void setSnatch(boolean snatch) {
        this.snatch = snatch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
