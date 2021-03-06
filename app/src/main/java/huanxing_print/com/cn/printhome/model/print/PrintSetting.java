package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LGH on 2017/3/21.
 */

public class PrintSetting implements Cloneable, Parcelable {

    private int colourFlag;
    private int directionFlag;
    private int doubleFlag;
    private String fileName;
    private int filePage;
    private String fileUrl;
    private int id;
    private int printCount;
    private int printerType;
    private int sizeType;
    private int scaleRatio;
    private int paperType;

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

    public int getFilePage() {
        return filePage;
    }

    public void setFilePage(int filePage) {
        this.filePage = filePage;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public int getPrinterType() {
        return printerType;
    }

    public void setPrinterType(int printerType) {
        this.printerType = printerType;
    }

    public int getSizeType() {
        return sizeType;
    }

    public void setSizeType(int sizeType) {
        this.sizeType = sizeType;
    }

    public int getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(int scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }

    @Override
    public PrintSetting clone() {
        PrintSetting printSetting = null;
        try {
            printSetting = (PrintSetting) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return printSetting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.colourFlag);
        dest.writeInt(this.directionFlag);
        dest.writeInt(this.doubleFlag);
        dest.writeString(this.fileName);
        dest.writeInt(this.filePage);
        dest.writeString(this.fileUrl);
        dest.writeInt(this.id);
        dest.writeInt(this.printCount);
        dest.writeInt(this.printerType);
        dest.writeInt(this.sizeType);
        dest.writeInt(this.scaleRatio);
        dest.writeInt(this.paperType);
    }

    public PrintSetting() {
    }

    protected PrintSetting(Parcel in) {
        this.colourFlag = in.readInt();
        this.directionFlag = in.readInt();
        this.doubleFlag = in.readInt();
        this.fileName = in.readString();
        this.filePage = in.readInt();
        this.fileUrl = in.readString();
        this.id = in.readInt();
        this.printCount = in.readInt();
        this.printerType = in.readInt();
        this.sizeType = in.readInt();
        this.scaleRatio = in.readInt();
        this.paperType = in.readInt();
    }

    public static final Parcelable.Creator<PrintSetting> CREATOR = new Parcelable.Creator<PrintSetting>() {
        @Override
        public PrintSetting createFromParcel(Parcel source) {
            return new PrintSetting(source);
        }

        @Override
        public PrintSetting[] newArray(int size) {
            return new PrintSetting[size];
        }
    };
}
