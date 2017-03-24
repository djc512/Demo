package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/24.
 */

public class OrderStatusBean extends CommonResp {
    private OrderStatus data;

    public OrderStatus getData() {
        return data;
    }

    public void setData(OrderStatus data) {
        this.data = data;
    }
}
