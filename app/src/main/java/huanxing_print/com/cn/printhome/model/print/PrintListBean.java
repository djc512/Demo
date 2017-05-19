package huanxing_print.com.cn.printhome.model.print;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrintListBean extends CommonResp {

    private List<FileInfo> data;

    public List<FileInfo> getData() {
        return data;
    }

    public void setData(List<FileInfo> data) {
        this.data = data;
    }

    //   addTime	时间戳	string
//    fileName	名称	string
//    filePage	文件页数	string
//    fileSize	文件大小 B	string
//    fileUrl	文件地址	string
//            previewUrl
    public static class FileInfo implements Parcelable {
        private String id;
        private String addTime;
        private String fileName;
        private String filePage;
        private String fileSize;
        private String fileUrl;
        private String previewUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePage() {
            return filePage;
        }

        public void setFilePage(String filePage) {
            this.filePage = filePage;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }

        @Override
        public String toString() {
            return "FileInfo{" +
                    "id='" + id + '\'' +
                    ", addTime='" + addTime + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", filePage='" + filePage + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    ", fileUrl='" + fileUrl + '\'' +
                    ", previewUrl='" + previewUrl + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.addTime);
            dest.writeString(this.fileName);
            dest.writeString(this.filePage);
            dest.writeString(this.fileSize);
            dest.writeString(this.fileUrl);
            dest.writeString(this.previewUrl);
        }

        public FileInfo() {
        }

        protected FileInfo(Parcel in) {
            this.id = in.readString();
            this.addTime = in.readString();
            this.fileName = in.readString();
            this.filePage = in.readString();
            this.fileSize = in.readString();
            this.fileUrl = in.readString();
            this.previewUrl = in.readString();
        }

        public static final Parcelable.Creator<FileInfo> CREATOR = new Parcelable.Creator<FileInfo>() {
            @Override
            public FileInfo createFromParcel(Parcel source) {
                return new FileInfo(source);
            }

            @Override
            public FileInfo[] newArray(int size) {
                return new FileInfo[size];
            }
        };
    }

}
