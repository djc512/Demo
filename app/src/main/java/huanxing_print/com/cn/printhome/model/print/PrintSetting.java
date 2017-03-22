package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LGH on 2017/3/21.
 */

public class PrintSetting implements Parcelable {

    int colourFlag;
    int directionFlag;
    int doubleFlag;
    int fileName;
    int filePage;
    int fileUrl;
    int id;
    int printCount;
    int printerType;
    int sizeType;

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

    public int getFileName() {
        return fileName;
    }

    public void setFileName(int fileName) {
        this.fileName = fileName;
    }

    public int getFilePage() {
        return filePage;
    }

    public void setFilePage(int filePage) {
        this.filePage = filePage;
    }

    public int getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(int fileUrl) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.colourFlag);
        dest.writeInt(this.directionFlag);
        dest.writeInt(this.doubleFlag);
        dest.writeInt(this.fileName);
        dest.writeInt(this.filePage);
        dest.writeInt(this.fileUrl);
        dest.writeInt(this.id);
        dest.writeInt(this.printCount);
        dest.writeInt(this.printerType);
        dest.writeInt(this.sizeType);
    }

    public PrintSetting() {
    }

    protected PrintSetting(Parcel in) {
        this.colourFlag = in.readInt();
        this.directionFlag = in.readInt();
        this.doubleFlag = in.readInt();
        this.fileName = in.readInt();
        this.filePage = in.readInt();
        this.fileUrl = in.readInt();
        this.id = in.readInt();
        this.printCount = in.readInt();
        this.printerType = in.readInt();
        this.sizeType = in.readInt();
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
