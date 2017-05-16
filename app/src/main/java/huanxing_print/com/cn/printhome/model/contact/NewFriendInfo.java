package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendInfo implements Parcelable {
    private int showType;//用于界面显示item
    private String memberId;//会员id
    private String memberName;//名称
    private String memberUrl;//头像
    private String note;//备注
    private String type;//0显示接受按钮,1已添加,2等待验证

    public NewFriendInfo() {

    }

    protected NewFriendInfo(Parcel in) {
        showType = in.readInt();
        memberId = in.readString();
        memberName = in.readString();
        memberUrl = in.readString();
        note = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(showType);
        dest.writeString(memberId);
        dest.writeString(memberName);
        dest.writeString(memberUrl);
        dest.writeString(note);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewFriendInfo> CREATOR = new Creator<NewFriendInfo>() {
        @Override
        public NewFriendInfo createFromParcel(Parcel in) {
            return new NewFriendInfo(in);
        }

        @Override
        public NewFriendInfo[] newArray(int size) {
            return new NewFriendInfo[size];
        }
    };

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
