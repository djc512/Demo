package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class UsedPrinterResp extends CommonResp {
    private List<Printer> data;

    public List<Printer> getData() {
        return data;
    }

    public void setData(List<Printer> data) {
        this.data = data;
    }

    //[{"addTime":1494401888000,"id":185,"memberId":"100000309","pageCount":"563",
    // "printerAddress":"江苏省南京市秦淮区东瓜匙路与明匙路交叉口西南150米","printerDef":0,"printerNo":"zwf001","printerType":"1",
    // "remark":"测试-教育机","remarkCount":"13","status":true,"updateTime":1494401902000}
    public static class Printer {
        private long addTime;
        private int id;
        private long memberId;
        private int pageCount;
        private String printerAddress;
        private int printerDef;
        private String printerNo;
        private String printerType;
        private String remark;
        private String remarkCount;
        private boolean status;
        private long updateTime;

        @Override
        public String toString() {
            return "PrintUtil{" +
                    "addTime=" + addTime +
                    ", id=" + id +
                    ", memberId=" + memberId +
                    ", pageCount=" + pageCount +
                    ", printerAddress='" + printerAddress + '\'' +
                    ", printerDef=" + printerDef +
                    ", printerNo='" + printerNo + '\'' +
                    ", printerType='" + printerType + '\'' +
                    ", remark='" + remark + '\'' +
                    ", remarkCount='" + remarkCount + '\'' +
                    ", status=" + status +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getMemberId() {
            return memberId;
        }

        public void setMemberId(long memberId) {
            this.memberId = memberId;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public String getPrinterAddress() {
            return printerAddress;
        }

        public void setPrinterAddress(String printerAddress) {
            this.printerAddress = printerAddress;
        }

        public int getPrinterDef() {
            return printerDef;
        }

        public void setPrinterDef(int printerDef) {
            this.printerDef = printerDef;
        }

        public String getPrinterNo() {
            return printerNo;
        }

        public void setPrinterNo(String printerNo) {
            this.printerNo = printerNo;
        }

        public String getPrinterType() {
            return printerType;
        }

        public void setPrinterType(String printerType) {
            this.printerType = printerType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemarkCount() {
            return remarkCount;
        }

        public void setRemarkCount(String remarkCount) {
            this.remarkCount = remarkCount;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }

}
