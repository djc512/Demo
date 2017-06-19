package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class OrderListBean {

    /**
     * printList : [{"addTime":"2017-05-10 16:22:35","amount":"0.10","color":1,"fileType":".docx","orderId":"30001920","orderStatus":5,"payType":"balance","printAddress":"普天科技园3楼黑白机","printPage":1,"printPrice":"0.1","printSize":0}]
     * month : 2017年05月
     */

    private String month;
    private List<PrintDetailBean> printList;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PrintDetailBean> getPrintList() {
        return printList;
    }

    public void setPrintList(List<PrintDetailBean> printList) {
        this.printList = printList;
    }


}
