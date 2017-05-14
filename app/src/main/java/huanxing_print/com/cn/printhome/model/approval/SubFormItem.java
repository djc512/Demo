package huanxing_print.com.cn.printhome.model.approval;

/**
 * 报销条目
 * Created by dd on 2017/5/9.
 */

public class SubFormItem {
    private String amount;//金额
    private String type;//类别
    private String approveId;
    private int id;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
