package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrinterPriceBean extends CommonResp {
    private Price data;

    public Price getData() {
        return data;
    }

    public void setData(Price data) {
        this.data = data;
    }
}
