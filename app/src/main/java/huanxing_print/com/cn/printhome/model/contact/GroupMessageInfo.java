package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 群设置信息
 * Created by wanghao on 2017/5/10.
 */

public class GroupMessageInfo implements Parcelable{
    private String balance;//群余额
    private String easemobGroupId;//群环信ID
    private String groupId;//群id
    private ArrayList<GroupMember> groupMembers;//群成员列表
    private String groupName;//群名称
    private String groupNotice;//群公告
    private String groupUrl;//群头像
    private String isManage;//是否是管理员 1是 0 不是
    public GroupMessageInfo() {

    }

    protected GroupMessageInfo(Parcel in) {
        balance = in.readString();
        easemobGroupId = in.readString();
        groupId = in.readString();
        groupMembers = in.createTypedArrayList(GroupMember.CREATOR);
        groupName = in.readString();
        groupNotice = in.readString();
        groupUrl = in.readString();
        isManage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(balance);
        dest.writeString(easemobGroupId);
        dest.writeString(groupId);
        dest.writeTypedList(groupMembers);
        dest.writeString(groupName);
        dest.writeString(groupNotice);
        dest.writeString(groupUrl);
        dest.writeString(isManage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupMessageInfo> CREATOR = new Creator<GroupMessageInfo>() {
        @Override
        public GroupMessageInfo createFromParcel(Parcel in) {
            return new GroupMessageInfo(in);
        }

        @Override
        public GroupMessageInfo[] newArray(int size) {
            return new GroupMessageInfo[size];
        }
    };

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEasemobGroupId() {
        return easemobGroupId;
    }

    public void setEasemobGroupId(String easemobGroupId) {
        this.easemobGroupId = easemobGroupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getGroupUrl() {
        return groupUrl;
    }

    public void setGroupUrl(String groupUrl) {
        this.groupUrl = groupUrl;
    }

    public String getIsManage() {
        return isManage;
    }

    public void setIsManage(String isManage) {
        this.isManage = isManage;
    }

}
