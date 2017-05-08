package huanxing_print.com.cn.printhome.model.approval;

/**
 * 审批对象
 * Created by dd on 2017/5/8.
 */

public class ApprovalObject {
    private String addTime;//申请时间
    private long amountMonney;//请款金额/总报销金额
    private long id;//审批id
    private String jobNumber;//申请人-工号
    private String purchaseList;//采购清单
    private long status;//审批状态
    private String title;//用途说明

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getAmountMonney() {
        return amountMonney;
    }

    public void setAmountMonney(long amountMonney) {
        this.amountMonney = amountMonney;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
