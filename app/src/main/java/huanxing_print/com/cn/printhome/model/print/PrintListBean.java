package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrintListBean extends CommonResp {

    private List<PrintList> data;

    public List<PrintList> getData() {
        return data;
    }

    public void setData(List<PrintList> data) {
        this.data = data;
    }
}
