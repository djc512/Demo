package huanxing_print.com.cn.printhome.event.contacts;

import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;

/**
 * Created by wanghao on 2017/5/22.
 */

public class GroupMessageUpdate {
    private String tag;
    private GroupMessageInfo groupMessageInfo;
    public GroupMessageUpdate(String tag, GroupMessageInfo groupMessageInfo) {
        this.tag = tag;
        this.groupMessageInfo = groupMessageInfo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public GroupMessageInfo getGroupMessageInfo() {
        return groupMessageInfo;
    }

    public void setGroupMessageInfo(GroupMessageInfo groupMessageInfo) {
        this.groupMessageInfo = groupMessageInfo;
    }
}
