package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/10.
 */

public class GroupMember implements Parcelable{
    private String easemobId;//环信id
    private String memberId;//用户id
    private String memberName;//用户名称
    private String memberUrl;//用户头像
    private String type;//群用户身份 0 普通 1群主
    public GroupMember() {

    }

    protected GroupMember(Parcel in) {
        easemobId = in.readString();
        memberId = in.readString();
        memberName = in.readString();
        memberUrl = in.readString();
        type = in.readString();
    }

    public static final Creator<GroupMember> CREATOR = new Creator<GroupMember>() {
        @Override
        public GroupMember createFromParcel(Parcel in) {
            return new GroupMember(in);
        }

        @Override
        public GroupMember[] newArray(int size) {
            return new GroupMember[size];
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(easemobId);
        parcel.writeString(memberId);
        parcel.writeString(memberName);
        parcel.writeString(memberUrl);
        parcel.writeString(type);
    }
}