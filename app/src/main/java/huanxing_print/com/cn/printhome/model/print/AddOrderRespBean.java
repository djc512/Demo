package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class AddOrderRespBean extends CommonResp {
    private Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
