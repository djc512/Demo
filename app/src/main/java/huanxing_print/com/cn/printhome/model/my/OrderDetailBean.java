package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class OrderDetailBean {

    /**
     * orderInfo : {"a3BlackPrice":"0.2","a3ColorPrice":"0.4","a4BlackPrice":"0.1","a4ColorPrice":"0.3","addNo":"100000271","addTime":1490169307000,"address":"","billStatus":0,"channel":"balance","companyName":"图牛环星","delFlag":0,"id":30000690,"printerName":"印家云打印-test002","printerNo":"48TZ-13102-1251581193","refundNo":"","status":5,"totalAmount":0.3,"tradeNo":"30000690","updateTime":1490169315000}
     * printFiles : [{"colourFlag":0,"directionFlag":1,"doubleFlag":1,"fileName":"img_0080.png","fileUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/19/Ci-4nVjSLdmALwJKAAnLadZDeSY670.png","printCount":1,"printNo":"48TZ-13102-1251581193","printType":"2","sizeType":0}]
     */

    private OrderInfoBean orderInfo;
    private List<PrintFilesBean> printFiles;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<PrintFilesBean> getPrintFiles() {
        return printFiles;
    }

    public void setPrintFiles(List<PrintFilesBean> printFiles) {
        this.printFiles = printFiles;
    }

    public static class OrderInfoBean {
        /**
         * a3BlackPrice : 0.2
         * a3ColorPrice : 0.4
         * a4BlackPrice : 0.1
         * a4ColorPrice : 0.3
         * addNo : 100000271
         * addTime : 1490169307000
         * address :
         * billStatus : 0
         * channel : balance
         * companyName : 图牛环星
         * delFlag : 0
         * id : 30000690
         * printerName : 印家云打印-test002
         * printerNo : 48TZ-13102-1251581193
         * refundNo :
         * status : 5
         * totalAmount : 0.3
         * tradeNo : 30000690
         * updateTime : 1490169315000
         */

        private String a3BlackPrice;
        private String a3ColorPrice;
        private String a4BlackPrice;
        private String a4ColorPrice;
        private String addNo;
        private long addTime;
        private String address;
        private int billStatus;
        private String channel;
        private String companyName;
        private int delFlag;
        private int id;
        private String printerName;
        private String printerNo;
        private String refundNo;
        private int status;
        private double totalAmount;
        private String tradeNo;
        private long updateTime;

        public String getA3BlackPrice() {
            return a3BlackPrice;
        }

        public void setA3BlackPrice(String a3BlackPrice) {
            this.a3BlackPrice = a3BlackPrice;
        }

        public String getA3ColorPrice() {
            return a3ColorPrice;
        }

        public void setA3ColorPrice(String a3ColorPrice) {
            this.a3ColorPrice = a3ColorPrice;
        }

        public String getA4BlackPrice() {
            return a4BlackPrice;
        }

        public void setA4BlackPrice(String a4BlackPrice) {
            this.a4BlackPrice = a4BlackPrice;
        }

        public String getA4ColorPrice() {
            return a4ColorPrice;
        }

        public void setA4ColorPrice(String a4ColorPrice) {
            this.a4ColorPrice = a4ColorPrice;
        }

        public String getAddNo() {
            return addNo;
        }

        public void setAddNo(String addNo) {
            this.addNo = addNo;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBillStatus() {
            return billStatus;
        }

        public void setBillStatus(int billStatus) {
            this.billStatus = billStatus;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrinterName() {
            return printerName;
        }

        public void setPrinterName(String printerName) {
            this.printerName = printerName;
        }

        public String getPrinterNo() {
            return printerNo;
        }

        public void setPrinterNo(String printerNo) {
            this.printerNo = printerNo;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class PrintFilesBean {

//        colourFlag	彩色打印0-彩色 1-黑白	number
//        directionFlag	方向标识0-横向 1-纵向	number
//        doubleFlag	双面打印0-是 1-否	number
//        fileName	文件名	string
//        fileUrl	文件url	string
//        printCount	打印份数	number
//        printNo	打印机编号	string
//        printType	打印类型	string
//        sizeType	大小类型0-A4 1-A3

        private int colourFlag;
        private int directionFlag;
        private int doubleFlag;
        private String fileName;
        private String fileUrl;
        private int printCount;
        private String printNo;
        private String printType;
        private int sizeType;

        public int getColourFlag() {
            return colourFlag;
        }

        public void setColourFlag(int colourFlag) {
            this.colourFlag = colourFlag;
        }

        public int getDirectionFlag() {
            return directionFlag;
        }

        public void setDirectionFlag(int directionFlag) {
            this.directionFlag = directionFlag;
        }

        public int getDoubleFlag() {
            return doubleFlag;
        }

        public void setDoubleFlag(int doubleFlag) {
            this.doubleFlag = doubleFlag;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getPrintCount() {
            return printCount;
        }

        public void setPrintCount(int printCount) {
            this.printCount = printCount;
        }

        public String getPrintNo() {
            return printNo;
        }

        public void setPrintNo(String printNo) {
            this.printNo = printNo;
        }

        public String getPrintType() {
            return printType;
        }

        public void setPrintType(String printType) {
            this.printType = printType;
        }

        public int getSizeType() {
            return sizeType;
        }

        public void setSizeType(int sizeType) {
            this.sizeType = sizeType;
        }
    }
}
