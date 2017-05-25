package huanxing_print.com.cn.printhome.model.my;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ChongZhiRecordListBean {

    /**
     * count : 10
     * list : [{"date":"2017年03月","detail":[{"amount":"0.01","context":"打印文件1份","date":"03月08日   00:29","type":0}]}]
     */

    private String date;
    private List<ChongZhiRecordListDetailBean> detail;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ChongZhiRecordListDetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<ChongZhiRecordListDetailBean> detail) {
        this.detail = detail;
    }
}
