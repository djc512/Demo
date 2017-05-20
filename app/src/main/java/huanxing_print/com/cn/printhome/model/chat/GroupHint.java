package huanxing_print.com.cn.printhome.model.chat;

/**
 * Created by dd on 2017/5/19.
 */

public class GroupHint {
    private String applyMemberId;
    private String groupId;
    private String memberId;
    private String message;
    private boolean isAll;
    private String type;

    public String getApplyMemberId() {
        return applyMemberId;
    }

    public void setApplyMemberId(String applyMemberId) {
        this.applyMemberId = applyMemberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
