package huanxing_print.com.cn.printhome.model.approval;

/**
 * 审批列表item对象
 * Created by dd on 2017/5/8.
 */

public class ApprovalObject {
    private String addTime;//申请时间
    private String amountMonney;//请款金额/总报销金额
    private String id;//审批id
    private String jobNumber;//申请人-工号
    private String purchaseList;//采购清单
    private int status;//审批状态
    private String title;//用途说明
    private int type;//类型  1 采购 2报销

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAmountMonney() {
        return amountMonney;
    }

    public void setAmountMonney(String amountMonney) {
        this.amountMonney = amountMonney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(String purchaseList) {
        this.purchaseList = purchaseList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
