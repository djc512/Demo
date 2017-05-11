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

    public static class Printer {
        //"data":[{"addTime":1488820433000,"id":28,"memberId":"100000091","pageCount":"52",
        // "printerAddress":"南京市玄武区黄家圩41号-A2-201","printerDef":0,"printerNo":"yangjiandeyiliao","remark":"测测测试",
        // "remarkCount":"1","status":false,"updateTime":1488820433000},
        private long addTime;
        private int id;
        private long memberId;
        private int pageCount;
        private String printerAddress;
        private int printerDef;
        private String printerNo;
        private String remark;
        private String remarkCount;
        private boolean status;
        private long updateTime;

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

        @Override
        public String toString() {
            return "Printer{" +
                    "addTime=" + addTime +
                    ", id=" + id +
                    ", memberId=" + memberId +
                    ", pageCount=" + pageCount +
                    ", printerAddress='" + printerAddress + '\'' +
                    ", printerDef=" + printerDef +
                    ", printerNo='" + printerNo + '\'' +
                    ", remark='" + remark + '\'' +
                    ", remarkCount='" + remarkCount + '\'' +
                    ", status=" + status +
                    ", updateTime=" + updateTime +
                    '}';
        }
    }

}
