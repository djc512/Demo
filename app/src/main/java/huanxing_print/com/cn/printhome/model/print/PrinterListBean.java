package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrinterListBean extends CommonResp {
    private List<Printer> data;

    public List<Printer> getData() {
        return data;
    }

    public void setData(List<Printer> data) {
        this.data = data;
    }
}
