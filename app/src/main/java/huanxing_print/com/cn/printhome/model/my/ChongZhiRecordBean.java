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
    private List<ChongZhiRecordListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ChongZhiRecordListBean> getList() {
        return list;
    }

    public void setList(List<ChongZhiRecordListBean> list) {
        this.list = list;
    }
}
