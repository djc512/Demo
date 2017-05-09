package huanxing_print.com.cn.printhome.model.approval;

/**
 * 审批人或抄送人
 * Created by dd on 2017/5/9.
 */

public class ApproverOrCopy {
    private String faceUrl;//头像
    private String jobNumber;//会员id
    private String name;//名字
    private long priority;//排序

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }
}
