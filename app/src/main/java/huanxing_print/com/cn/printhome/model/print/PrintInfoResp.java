package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/10.
 */

public class PrintInfoResp extends CommonResp {

    private PrinterPrice data;

    public PrinterPrice getData() {
        return data;
    }

    public void setData(PrinterPrice data) {
        this.data = data;
    }

    //"a3BlackPrice":"0.3","a3ColorPrice":"0.4","a4BlackPrice":"0.1","a4ColorPrice":"0.2","addTime":1490925707000,
    // "capability":"黑白  彩色","companyName":"南京田中机电再制造有限公司高淳分公司","delFlag":0,"id":48,
    // "printAddress":"江苏省南京市秦淮区东瓜匙路与明匙路交叉口西南150米","printName":"测试-教育机","printerNo":"zwf001","printerType":"1",
    // "resolution":"1200*1200dpi","technicalType":"仅支持激光","updateTime":1490925707000
    public static class PrinterPrice implements Parcelable {
        private String a3BlackPrice;
        private String a3ColorPrice;
        private String a4BlackPrice;
        private String a4ColorPrice;
        private String capability;
        private String companyName;
        private String printAddress;
        private String printerType;
        private String telPhone;
        private String printName;
        private String resolution;
        private String technicalType;
        private String printerNo;

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

        public String getCapability() {
            return capability;
        }

        public void setCapability(String capability) {
            this.capability = capability;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPrintAddress() {
            return printAddress;
        }

        public void setPrintAddress(String printAddress) {
            this.printAddress = printAddress;
        }

        public String getPrinterType() {
            return printerType;
        }

        public void setPrinterType(String printerType) {
            this.printerType = printerType;
        }

        public String getTelPhone() {
            return telPhone;
        }

        public void setTelPhone(String telPhone) {
            this.telPhone = telPhone;
        }

        public String getPrintName() {
            return printName;
        }

        public void setPrintName(String printName) {
            this.printName = printName;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getTechnicalType() {
            return technicalType;
        }

        public void setTechnicalType(String technicalType) {
            this.technicalType = technicalType;
        }

        public String getPrinterNo() {
            return printerNo;
        }

        public void setPrinterNo(String printerNo) {
            this.printerNo = printerNo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.a3BlackPrice);
            dest.writeString(this.a3ColorPrice);
            dest.writeString(this.a4BlackPrice);
            dest.writeString(this.a4ColorPrice);
            dest.writeString(this.capability);
            dest.writeString(this.companyName);
            dest.writeString(this.printAddress);
            dest.writeString(this.printerType);
            dest.writeString(this.telPhone);
            dest.writeString(this.printName);
            dest.writeString(this.resolution);
            dest.writeString(this.technicalType);
            dest.writeString(this.printerNo);
        }

        public PrinterPrice() {
        }

        protected PrinterPrice(Parcel in) {
            this.a3BlackPrice = in.readString();
            this.a3ColorPrice = in.readString();
            this.a4BlackPrice = in.readString();
            this.a4ColorPrice = in.readString();
            this.capability = in.readString();
            this.companyName = in.readString();
            this.printAddress = in.readString();
            this.printerType = in.readString();
            this.telPhone = in.readString();
            this.printName = in.readString();
            this.resolution = in.readString();
            this.technicalType = in.readString();
            this.printerNo = in.readString();
        }

        public static final Parcelable.Creator<PrinterPrice> CREATOR = new Parcelable.Creator<PrinterPrice>() {
            @Override
            public PrinterPrice createFromParcel(Parcel source) {
                return new PrinterPrice(source);
            }

            @Override
            public PrinterPrice[] newArray(int size) {
                return new PrinterPrice[size];
            }
        };
    }

}
