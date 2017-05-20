package huanxing_print.com.cn.printhome.model.chat;

import java.util.ArrayList;

/**
 * Created by dd on 2017/5/15.
 */

public class GroupLuckyPackageDetail {
    private String amount;
    private boolean invalid;
    private ArrayList<LuckyPackageObject> list;
    private String outTime;
    private String packetId;
    private String remark;
    private String sendMemberId;
    private String sendMemberName;
    private String sendMemberUrl;
    private boolean snatch;
    private String snatchAmount;
    private String snatchNum;
    private String totalAmount;
    private String totalNumber;

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

    public boolean isSnatch() {
        return snatch;
    }

    public void setSnatch(boolean snatch) {
        this.snatch = snatch;
    }

    public ArrayList<LuckyPackageObject> getList() {
        return list;
    }

    public void setList(ArrayList<LuckyPackageObject> list) {
        this.list = list;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
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


    public String getSnatchAmount() {
        return snatchAmount;
    }

    public void setSnatchAmount(String snatchAmount) {
        this.snatchAmount = snatchAmount;
    }

    public String getSnatchNum() {
        return snatchNum;
    }

    public void setSnatchNum(String snatchNum) {
        this.snatchNum = snatchNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }
}
