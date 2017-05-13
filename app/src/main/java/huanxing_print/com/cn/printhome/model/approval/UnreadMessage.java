package huanxing_print.com.cn.printhome.model.approval;

/**
 * Created by dd on 2017/5/9.
 */

public class UnreadMessage {
    private int approverNum;//待我审批
    private int copyerNum;//抄送我的
    private int initiatorNum;//我发起的
    private int total;//总数量

    public int getApproverNum() {
        return approverNum;
    }

    public void setApproverNum(int approverNum) {
        this.approverNum = approverNum;
    }

    public int getCopyerNum() {
        return copyerNum;
    }

    public void setCopyerNum(int copyerNum) {
        this.copyerNum = copyerNum;
    }

    public int getInitiatorNum() {
        return initiatorNum;
    }

    public void setInitiatorNum(int initiatorNum) {
        this.initiatorNum = initiatorNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
