package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/5.
 */

public class AroundPrinterResp extends CommonResp {

    private PrinterList data;

    public PrinterList getData() {
        return data;
    }

    public void setData(PrinterList data) {
        this.data = data;
    }

    public static class PrinterList {
        private List<Printer> list;
        private String local;

        public List<Printer> getList() {
            return list;
        }

        public void setList(List<Printer> list) {
            this.list = list;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }
    }

    //    {"address":"江苏省南京市秦淮区东瓜匙路与明匙路交叉口西南150米","distance":"46","isOnline":"true","location":"118.790521,
    // 31.998919","name":"测试-教育机","pageCount":"558","printerNo":"zwf001","printerType":"1","remarkCount":"12"}
    public static class Printer {
        private String address;
        private String distance;
        private boolean isOnline;
        private String location;
        private String name;
        private String pageCount;
        private String printerNo;
        private String printerType;
        private String remarkCount;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
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

        public String getRemarkCount() {
            return remarkCount;
        }

        public void setRemarkCount(String remarkCount) {
            this.remarkCount = remarkCount;
        }
    }


}
