package huanxing_print.com.cn.printhome.event.contacts;

/**
 * Created by wanghao on 2017/5/17.
 */

public class GroupUpdate {
    private String update;

    public GroupUpdate(String update) {
        this.update = update;
    }

    public String getResultMessage() {
        return update;
    }

    public void setResultMessage(String update) {
        this.update = update;
    }

}
