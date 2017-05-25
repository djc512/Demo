package huanxing_print.com.cn.printhome.event.chat;

/**
 * Created by wanghao on 2017/5/25.
 */

public class GroupMessageUpdateInRed {
    private String tag;
    private String groupId;
    private String member;
    private boolean isAdd = false;
    public GroupMessageUpdateInRed(String tag, String groupId, String member, boolean isAdd) {
        this.tag = tag;
        this.groupId = groupId;
        this.member = member;
        this.isAdd = isAdd;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
