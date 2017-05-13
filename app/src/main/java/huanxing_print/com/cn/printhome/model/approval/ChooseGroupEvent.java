package huanxing_print.com.cn.printhome.model.approval;

/**
 * 选择人员时的回调
 * Created by dd on 2017/5/13.
 */

public class ChooseGroupEvent {
    public String groupId;

    public ChooseGroupEvent(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
