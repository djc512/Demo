package huanxing_print.com.cn.printhome.model.chat;

import java.util.ArrayList;

/**
 * Created by dd on 2017/5/15.
 */

public class GroupLuckyPackageDetail {
    private ArrayList<LuckyPackageObject> detail;
    private String masterMemberUrl;
    private String masterName;
    private String outTime;
    private String remark;
    private String snatchAmount;
    private String snatchNum;
    private String totalAmount;
    private String totalNumber;

    public ArrayList<LuckyPackageObject> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<LuckyPackageObject> detail) {
        this.detail = detail;
    }

    public String getMasterMemberUrl() {
        return masterMemberUrl;
    }

    public void setMasterMemberUrl(String masterMemberUrl) {
        this.masterMemberUrl = masterMemberUrl;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
