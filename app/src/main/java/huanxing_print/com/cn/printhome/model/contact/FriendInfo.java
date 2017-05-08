package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/8.
 */

public class FriendInfo implements Parcelable{
    private String easemobId;//环信id
    private String memberId;//会员id
    private String memberName;//名称
    private String memberUrl;//头像
    private String phone;//电话
    private String uniqueId;//印家号
    private int type;//用于联系人页面item类型
    public FriendInfo(){

    }

    protected FriendInfo(Parcel in) {
        easemobId = in.readString();
        memberId = in.readString();
        memberName = in.readString();
        memberUrl = in.readString();
        phone = in.readString();
        uniqueId = in.readString();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(easemobId);
        dest.writeString(memberId);
        dest.writeString(memberName);
        dest.writeString(memberUrl);
        dest.writeString(phone);
        dest.writeString(uniqueId);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendInfo> CREATOR = new Creator<FriendInfo>() {
        @Override
        public FriendInfo createFromParcel(Parcel in) {
            return new FriendInfo(in);
        }

        @Override
        public FriendInfo[] newArray(int size) {
            return new FriendInfo[size];
        }
    };

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
