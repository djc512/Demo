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

    public static class Order {

        private String orderId;
        private String totalAmount;
        private String totleBalance;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getTotleBalance() {
            return totleBalance;
        }

        public void setTotleBalance(String totleBalance) {
            this.totleBalance = totleBalance;
        }
    }
}
