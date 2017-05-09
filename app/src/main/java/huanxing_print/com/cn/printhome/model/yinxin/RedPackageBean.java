package huanxing_print.com.cn.printhome.model.yinxin;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/8.
 */

public class RedPackageBean extends CommonResp {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    //"masterMemberUrl":"测试内容k8ri","masterName":"qiyanyan","remark":"hello","snatchAmount":46026,"snatchNum":2,
    // "totalAmount":11001,"totalNumber":5
    public static class Data {
        private List<Detail> detail;
        private String masterMemberUrl;
        private String masterName;
        private String remark;
        private int snatchAmount;
        private int snatchNum;
        private int totalAmount;
        private int totalNumber;

        public List<Detail> getDetail() {
            return detail;
        }

        public void setDetail(List<Detail> detail) {
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getSnatchAmount() {
            return snatchAmount;
        }

        public void setSnatchAmount(int snatchAmount) {
            this.snatchAmount = snatchAmount;
        }

        public int getSnatchNum() {
            return snatchNum;
        }

        public void setSnatchNum(int snatchNum) {
            this.snatchNum = snatchNum;
        }

        public int getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }
    }


    // "amount":62483,"faceUrl":"测试内容of61","name":"zhang","time":"17:53:26"
    public static class Detail {
        private int amount;
        private String faceUrl;
        private String name;
        private String time;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
