package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * 上次的审批人对象
 * Created by dd on 2017/5/9.
 */

public class LastApprover {

    private ArrayList<ApproverOrCopy> approverList;
    private ArrayList<ApproverOrCopy> copyerList;

    public ArrayList<ApproverOrCopy> getApproverList() {
        return approverList;
    }

    public void setApproverList(ArrayList<ApproverOrCopy> approverList) {
        this.approverList = approverList;
    }

    public ArrayList<ApproverOrCopy> getCopyerList() {
        return copyerList;
    }

    public void setCopyerList(ArrayList<ApproverOrCopy> copyerList) {
        this.copyerList = copyerList;
    }
}
