package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/4.
 */

public class PhoneContactInfo implements Parcelable {
    private int type;//recycler item类型
    private String telName;
    private String yjName;
    private String yjNum;
    private String telNo;
    private String verification;
    private String icon;
    private int friendState;

    public enum STATE {
        UNREGISTED, NOTFRIEND, FRIEND
    }

    public PhoneContactInfo() {

    }

    protected PhoneContactInfo(Parcel in) {
        type = in.readInt();
        telName = in.readString();
        yjName = in.readString();
        yjNum = in.readString();
        telNo = in.readString();
        verification = in.readString();
        icon = in.readString();
        friendState = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(telName);
        dest.writeString(yjName);
        dest.writeString(yjNum);
        dest.writeString(telNo);
        dest.writeString(verification);
        dest.writeString(icon);
        dest.writeInt(friendState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneContactInfo> CREATOR = new Creator<PhoneContactInfo>() {
        @Override
        public PhoneContactInfo createFromParcel(Parcel in) {
            return new PhoneContactInfo(in);
        }

        @Override
        public PhoneContactInfo[] newArray(int size) {
            return new PhoneContactInfo[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getYjName() {
        return yjName;
    }

    public void setYjName(String yjName) {
        this.yjName = yjName;
    }

    public String getYjNum() {
        return yjNum;
    }

    public void setYjNum(String yjNum) {
        this.yjNum = yjNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFriendState() {
        return friendState;
    }

    public void setFriendState(int friendState) {
        this.friendState = friendState;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
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
}
