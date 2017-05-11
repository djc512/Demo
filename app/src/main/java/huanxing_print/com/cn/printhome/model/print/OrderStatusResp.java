package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/24.
 */

public class OrderStatusResp extends CommonResp {
    private OrderStatus data;

    public OrderStatus getData() {
        return data;
    }

    public void setData(OrderStatus data) {
        this.data = data;
    }

    public static class OrderStatus {
        private boolean needAwake;
        private int waitingCount;
        private int status;

        public boolean isNeedAwake() {
            return needAwake;
        }

        public void setNeedAwake(boolean needAwake) {
            this.needAwake = needAwake;
        }

        public int getWaitingCount() {
            return waitingCount;
        }

        public void setWaitingCount(int waitingCount) {
            this.waitingCount = waitingCount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
