package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiRecordBean {

    /**
     * count : 10
     * list : [{"date":"2017年03月","detail":[{"amount":"0.01","context":"打印文件1份","date":"03月08日   00:29","type":0}]}]
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
         * date : 2017年03月
         * detail : [{"amount":"0.01","context":"打印文件1份","date":"03月08日   00:29","type":0}]
         */

        private String date;
        private List<DetailBean> detail;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public static class DetailBean {
            /**
             * amount : 0.01
             * context : 打印文件1份
             * date : 03月08日   00:29
             * type : 0
             */

            private String amount;
            private String context;
            private String date;
            private int type;
            private int  orderId;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }
        }
    }
}
