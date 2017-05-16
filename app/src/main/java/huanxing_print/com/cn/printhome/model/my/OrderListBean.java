package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class OrderListBean {

    /**
     * printList : [{"addTime":"2017-05-10 16:22:35","amount":"0.10","color":1,"fileType":".docx","orderId":"30001920","orderStatus":5,"payType":"balance","printAddress":"普天科技园3楼黑白机","printPage":1,"printPrice":"0.1","printSize":0}]
     * month : 2017年05月
     */

    private String month;
    private List<PrintListBean> printList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PrintListBean> getPrintList() {
        return printList;
    }

    public void setPrintList(List<PrintListBean> printList) {
        this.printList = printList;
    }

    public static class PrintListBean {
        /**
         * addTime : 2017-05-10 16:22:35
         * amount : 0.10
         * color : 1
         * fileType : .docx
         * orderId : 30001920
         * orderStatus : 5
         * payType : balance
         * printAddress : 普天科技园3楼黑白机
         * printPage : 1
         * printPrice : 0.1
         * printSize : 0
         */

        private String addTime;
        private String amount;
        private int color;
        private String fileType;
        private String orderId;
        private int orderStatus;
        private String payType;
        private String printAddress;
        private int printPage;
        private String printPrice;
        private int printSize;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getPrintAddress() {
            return printAddress;
        }

        public void setPrintAddress(String printAddress) {
            this.printAddress = printAddress;
        }

        public int getPrintPage() {
            return printPage;
        }

        public void setPrintPage(int printPage) {
            this.printPage = printPage;
        }

        public String getPrintPrice() {
            return printPrice;
        }

        public void setPrintPrice(String printPrice) {
            this.printPrice = printPrice;
        }

        public int getPrintSize() {
            return printSize;
        }

        public void setPrintSize(int printSize) {
            this.printSize = printSize;
        }
    }
}
