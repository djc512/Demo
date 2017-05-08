package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/5.
 */

public class AroundPrinterListBean extends CommonResp {

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

    // "distance":"1126","isOnline":"测试内容8v02","latitude":1,"location":"118.803634,32.092459","longitude":1,
    // "name":"印家云打印-test001","pageCount":"243","printerNo":"111222333000","printerType":1,"remarkCount":"0"
    public static class Printer {
        private String distance;
        private String isOnline;
        private String latitude;
        private String location;
        private String longitude;
        private String name;
        private String pageCount;
        private String printerNo;
        private String printerType;
        private String remarkCount;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(String isOnline) {
            this.isOnline = isOnline;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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
