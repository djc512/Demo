package huanxing_print.com.cn.printhome.model.approval;

/**
 * 审批人或抄送人
 * Created by dd on 2017/5/9.
 */

public class ApprovalPeopleItem {
    private String faceUrl;//头像
    private String jobNumber;//会员id
    private String name;//名字
    private String priority;//排序
    private String status;//-1-（默认）未开始、0-审批中、2-已同意/3-已拒绝、4-申请人撤销、5-完成-未读、6-完成-
    private String type;//0-申请人；1-审批人；2-抄送人
    private String updateTime;//更新时间

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
