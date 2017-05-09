package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupInfo implements Parcelable{
    private String balance;//群余额
    private String easemobGroupId;//环信群id
    private String groupId;//印家群id
    private String groupName;//群名称
    private String groupUrl;//群头像
    private String userCount;//群成员个数
    public GroupInfo() {

    }

    protected GroupInfo(Parcel in) {
        balance = in.readString();
        easemobGroupId = in.readString();
        groupId = in.readString();
        groupName = in.readString();
        groupUrl = in.readString();
        userCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(balance);
        dest.writeString(easemobGroupId);
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeString(groupUrl);
        dest.writeString(userCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupInfo> CREATOR = new Creator<GroupInfo>() {
        @Override
        public GroupInfo createFromParcel(Parcel in) {
            return new GroupInfo(in);
        }

        @Override
        public GroupInfo[] newArray(int size) {
            return new GroupInfo[size];
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

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
}
