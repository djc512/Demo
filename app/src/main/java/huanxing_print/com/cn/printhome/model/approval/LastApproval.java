package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * 上次的审批人对象
 * Created by dd on 2017/5/9.
 */

public class LastApproval {

    private ArrayList<ApprovalOrCopy> approverList;
    private ArrayList<ApprovalOrCopy> copyerList;
    private String groupId;

    public ArrayList<ApprovalOrCopy> getApproverList() {
        return approverList;
    }

    public void setApproverList(ArrayList<ApprovalOrCopy> approverList) {
        this.approverList = approverList;
    }

    public ArrayList<ApprovalOrCopy> getCopyerList() {
        return copyerList;
    }

    public void setCopyerList(ArrayList<ApprovalOrCopy> copyerList) {
        this.copyerList = copyerList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
