package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.model.contact.GroupMember;

/**
 * 选择人员时的回调
 * Created by dd on 2017/5/13.
 */

public class ChooseMemberEvent {
    public int msgCode;
    public ArrayList<GroupMember> groupMembers;

    public ChooseMemberEvent(int msgCode, ArrayList<GroupMember> groupMembers) {
        this.msgCode = msgCode;
        this.groupMembers = groupMembers;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public ArrayList<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
