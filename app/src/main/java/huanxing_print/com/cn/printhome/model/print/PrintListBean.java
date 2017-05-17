package huanxing_print.com.cn.printhome.model.print;

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
    public static class FileInfo {
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
    }

}
