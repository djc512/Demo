package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 印家号搜索结果对象／通讯录手机号查询
 * Created by wanghao on 2017/5/8.
 */

public class FriendSearchInfo implements Parcelable{
    private String easemobId;//环信id
    private String faceUrl;//用户头像
    private String memberId;//会员id
    private String memberName;//会员名称
    private String memberUrl;//会员头像
    private String name;//
    private String nickName;//名称
    private String uniqueId;//印家号
    private int isFriend;//是否是好友 1是 0 否
    private int isMember;//是否是印家会员 1是 0否
    private String telName;//手机号码联系人名称
    private String telNo;//手机号码

    public FriendSearchInfo() {

    }

    protected FriendSearchInfo(Parcel in) {
        easemobId = in.readString();
        faceUrl = in.readString();
        memberId = in.readString();
        memberName = in.readString();
        memberUrl = in.readString();
        name = in.readString();
        nickName = in.readString();
        uniqueId = in.readString();
        isFriend = in.readInt();
        isMember = in.readInt();
        telName = in.readString();
        telNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(easemobId);
        dest.writeString(faceUrl);
        dest.writeString(memberId);
        dest.writeString(memberName);
        dest.writeString(memberUrl);
        dest.writeString(name);
        dest.writeString(nickName);
        dest.writeString(uniqueId);
        dest.writeInt(isFriend);
        dest.writeInt(isMember);
        dest.writeString(telName);
        dest.writeString(telNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendSearchInfo> CREATOR = new Creator<FriendSearchInfo>() {
        @Override
        public FriendSearchInfo createFromParcel(Parcel in) {
            return new FriendSearchInfo(in);
        }

        @Override
        public FriendSearchInfo[] newArray(int size) {
            return new FriendSearchInfo[size];
        }
    };

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberUrl() {
        return memberUrl;
    }

    public void setMemberUrl(String memberUrl) {
        this.memberUrl = memberUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTelName() {
        return telName;
    }

    public void setTelName(String telName) {
        this.telName = telName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }
}
