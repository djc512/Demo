package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * Created by dd on 2017/5/9.
 */

public class UnreadMessage {
    private long totle;//总共
    private ArrayList<Message> list;//消息集合

    public long getTotle() {
        return totle;
    }

    public void setTotle(long totle) {
        this.totle = totle;
    }

    public ArrayList<Message> getList() {
        return list;
    }

    public void setList(ArrayList<Message> list) {
        this.list = list;
    }
}
