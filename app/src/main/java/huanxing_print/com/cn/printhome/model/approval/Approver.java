package huanxing_print.com.cn.printhome.model.approval;

/**
 * 上传的审批人
 * Created by dd on 2017/5/12.
 */

public class Approver {
    private String jobNumber;//审批人id
    private int priority;//顺序

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
