package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * 上次的审批人对象
 * Created by dd on 2017/5/9.
 */

public class LastApproval {

    private ArrayList<ApprovalPeopleItem> approverList;
    private ArrayList<ApprovalCopyPeopleItem> copyList;
    private String groupId;

    public ArrayList<ApprovalPeopleItem> getApproverList() {
        return approverList;
    }

    public void setApproverList(ArrayList<ApprovalPeopleItem> approverList) {
        this.approverList = approverList;
    }

    public ArrayList<ApprovalCopyPeopleItem> getCopyList() {
        return copyList;
    }

    public void setCopyList(ArrayList<ApprovalCopyPeopleItem> copyList) {
        this.copyList = copyList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
