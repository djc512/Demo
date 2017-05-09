package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/9.
 */

public class PreviewResp extends CommonResp {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<String> fileUrlList;
        private String paperNum;

        public List<String> getFileUrlList() {
            return fileUrlList;
        }

        public void setFileUrlList(List<String> fileUrlList) {
            this.fileUrlList = fileUrlList;
        }

        public String getPaperNum() {
            return paperNum;
        }

        public void setPaperNum(String paperNum) {
            this.paperNum = paperNum;
        }
    }
}
