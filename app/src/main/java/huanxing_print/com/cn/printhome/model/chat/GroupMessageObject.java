package huanxing_print.com.cn.printhome.model.chat;

import java.io.Serializable;

/**
 * Created by dd on 2017/5/25.
 */

public class GroupMessageObject implements Serializable {
    private String groupId;
    private String groupEaseId;
    private String groupName;
    private String groupUrl;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupEaseId() {
        return groupEaseId;
    }

    public void setGroupEaseId(String groupEaseId) {
        this.groupEaseId = groupEaseId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUrl() {
        return groupUrl;
    }

    public void setGroupUrl(String groupUrl) {
        this.groupUrl = groupUrl;
    }
}
