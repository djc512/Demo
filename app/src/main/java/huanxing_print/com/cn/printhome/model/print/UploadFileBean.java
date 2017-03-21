package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/20.
 */

public class UploadFileBean extends CommonResp {

    private List<PrintSetting> data;

    public List<PrintSetting> getData() {
        return data;
    }

    public void setData(List<PrintSetting> data) {
        this.data = data;
    }

    class PrintSetting {
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
    }

}
