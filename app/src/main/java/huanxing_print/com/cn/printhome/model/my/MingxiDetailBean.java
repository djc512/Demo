package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class MingxiDetailBean {

    /**
     * count : 10
     * list : [{"date":"2017年03月","detail":[{"amount":"0.01","context":"打印文件1份","date":"03月08日   00:29","type":0}],"monthAmount":"测试内容fmx1"}]
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
         * monthAmount : 测试内容fmx1
         */

        private String date;
        private String monthAmount;
        private List<DetailBean> detail;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMonthAmount() {
            return monthAmount;
        }

        public void setMonthAmount(String monthAmount) {
            this.monthAmount = monthAmount;
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
        }
    }
}
