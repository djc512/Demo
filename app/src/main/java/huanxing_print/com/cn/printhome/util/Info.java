package huanxing_print.com.cn.printhome.util.approval;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class Info {
    private String name;
    private String time;
    private String detail;//明细
    private String use;
    private int id;//是否审批中

    public Info(String name, String time, String detail, String use, int id) {
        this.name = name;
        this.time = time;
        this.detail = detail;
        this.use = use;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
