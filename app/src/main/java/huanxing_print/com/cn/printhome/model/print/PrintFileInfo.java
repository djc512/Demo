package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LGH on 2017/5/25.
 */

public class PrintFileInfo implements Parcelable {

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_COPY = 2;
    public static final int TYPE_FILE = 3;

    private int fileType;
    private int pageCount;

    public PrintFileInfo(int fileType, int pageCount) {
        this.fileType = fileType;
        this.pageCount = pageCount;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "PrintFileInfo{" +
                "fileType=" + fileType +
                ", pageCount=" + pageCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fileType);
        dest.writeInt(this.pageCount);
    }

    protected PrintFileInfo(Parcel in) {
        this.fileType = in.readInt();
        this.pageCount = in.readInt();
    }

    public static final Parcelable.Creator<PrintFileInfo> CREATOR = new Parcelable.Creator<PrintFileInfo>() {
        @Override
        public PrintFileInfo createFromParcel(Parcel source) {
            return new PrintFileInfo(source);
        }

        @Override
        public PrintFileInfo[] newArray(int size) {
            return new PrintFileInfo[size];
        }
    };
}
