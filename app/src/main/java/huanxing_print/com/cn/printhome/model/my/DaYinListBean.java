package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DaYinListBean {

    /**
     * data : {"count":5,"list":[{"fileInfos":[{"fileName":"img_0080.png","filePage":1,"printCount":1}],"status":70581}]}
     * errorCode : 0
     * errorMsg :
     * success : true
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * count : 5
         * list : [{"fileInfos":[{"fileName":"img_0080.png","filePage":1,"printCount":1}],"status":70581}]
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
             * fileInfos : [{"fileName":"img_0080.png","filePage":1,"printCount":1}]
             * status : 70581
             */

            private int status;
            private List<FileInfosBean> fileInfos;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<FileInfosBean> getFileInfos() {
                return fileInfos;
            }

            public void setFileInfos(List<FileInfosBean> fileInfos) {
                this.fileInfos = fileInfos;
            }

            public static class FileInfosBean {
                /**
                 * fileName : img_0080.png
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
}
