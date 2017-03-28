package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DaYinListBean {

    /**
     * count : 39
     * list : [{"addTime":"2017-03-07 09:47:58","companyName":"南京图妞妞","fileInfos":[{"fileName":"img_2108.jpg","filePage":1,"printCount":1}],"fileNum":1,"id":10000607,"printNo":"yangjiandeyiliao","printerName":"测测测试","remarkId":0,"status":5,"timeStamp":"20170307094758000","totalAmount":0.01},{"addTime":"2017-03-07 01:18:28","companyName":"南京图妞妞","fileInfos":[{"fileName":"airbnb旅行行程单.pdf","filePage":1,"printCount":1}],"fileNum":1,"id":10000589,"printNo":"yangjiandeyiliao","printerName":"测测测试","remarkId":0,"status":5,"timeStamp":"20170307011828000","totalAmount":0.01},{"addTime":"2017-03-07 01:13:53","companyName":"南京图妞妞","fileInfos":[{"fileName":"img_2106.jpg","filePage":1,"printCount":1}],"fileNum":1,"id":10000585,"printNo":"yangjiandeyiliao","printerName":"测测测试","remarkId":0,"status":5,"timeStamp":"20170307011353000","totalAmount":0.01},{"addTime":"2017-03-07 00:09:28","companyName":"南京图妞妞","fileInfos":[{"fileName":"img_2106.jpg","filePage":1,"printCount":1}],"fileNum":1,"id":10000577,"printNo":"00009","printerName":"ceshi2","remarkId":0,"status":5,"timeStamp":"20170307000928000","totalAmount":0.01},{"addTime":"2017-03-06 23:14:05","companyName":"南京图妞妞","fileInfos":[{"fileName":"img_2106.jpg","filePage":1,"printCount":1}],"fileNum":1,"id":10000571,"printNo":"00009","printerName":"ceshi2","remarkId":0,"status":5,"timeStamp":"20170306231405000","totalAmount":0.01},{"addTime":"2017-03-06 23:05:45","companyName":"南京图妞妞","fileInfos":[{"fileName":"img_2106.jpg","filePage":1,"printCount":1}],"fileNum":1,"id":10000569,"printNo":"00009","printerName":"ceshi2","remarkId":0,"status":5,"timeStamp":"20170306230545000","totalAmount":0.01}]
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * addTime : 2017-03-07 09:47:58
         * companyName : 南京图妞妞
         * fileInfos : [{"fileName":"img_2108.jpg","filePage":1,"printCount":1}]
         * fileNum : 1
         * id : 10000607
         * printNo : yangjiandeyiliao
         * printerName : 测测测试
         * remarkId : 0
         * status : 5
         * timeStamp : 20170307094758000
         * totalAmount : 0.01
         */

        private String addTime;
        private String companyName;
        private int fileNum;
        private int id;
        private String printNo;
        private String printerName;
        private int remarkId;
        private int status;
        private String timeStamp;
        private double totalAmount;
        private List<FileInfosBean> fileInfos;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public int getFileNum() {
            return fileNum;
        }

        public void setFileNum(int fileNum) {
            this.fileNum = fileNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrintNo() {
            return printNo;
        }

        public void setPrintNo(String printNo) {
            this.printNo = printNo;
        }

        public String getPrinterName() {
            return printerName;
        }

        public void setPrinterName(String printerName) {
            this.printerName = printerName;
        }

        public int getRemarkId() {
            return remarkId;
        }

        public void setRemarkId(int remarkId) {
            this.remarkId = remarkId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<FileInfosBean> getFileInfos() {
            return fileInfos;
        }

        public void setFileInfos(List<FileInfosBean> fileInfos) {
            this.fileInfos = fileInfos;
        }

        public static class FileInfosBean {
            /**
             * fileName : img_2108.jpg
             * filePage : 1
             * printCount : 1
             */

            private String fileName;
            private int filePage;
            private int printCount;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public int getFilePage() {
                return filePage;
            }

            public void setFilePage(int filePage) {
                this.filePage = filePage;
            }

            public int getPrintCount() {
                return printCount;
            }

            public void setPrintCount(int printCount) {
                this.printCount = printCount;
            }
        }
    }
}
