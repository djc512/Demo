package huanxing_print.com.cn.printhome.model.print;

/**
 * Created by LGH on 2017/3/23.
 */

public class Price {
    // "a3BlackPrice":"0.05","a3ColorPrice":"0.01","a4BlackPrice":"0.01","a4ColorPrice":"0.01",
    // "capability":"测试内容2m21","companyName":"南京图妞妞","printAddress":"测试内容iyxp","printName":"ceshi",
    // "printerType":"测试内容tty7","telPhone":"测试内容4d2b"

    private String a3BlackPrice;
    private String a3ColorPrice;
    private String a4BlackPrice;
    private String a4ColorPrice;
    private String capability;
    private String companyName;
    private String printAddress;
    private String printName;
    private String printerType;
    private String telPhone;

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

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
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
}
