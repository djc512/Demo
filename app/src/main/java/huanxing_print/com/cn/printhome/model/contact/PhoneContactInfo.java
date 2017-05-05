package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/4.
 */

public class PhoneContactInfo implements Parcelable {
    private int type;//recycler item类型
    private String phoneName;
    private String yjName;
    private String yjNum;
    private String phoneNum;
    private String verification;
    private String icon;
    private int friendState;

    public enum STATE{
        UNREGISTED,NOTFRIEND,FRIEND
    }

    public PhoneContactInfo() {

    }

    protected PhoneContactInfo(Parcel in) {
        type = in.readInt();
        phoneName = in.readString();
        yjName = in.readString();
        yjNum = in.readString();
        phoneNum = in.readString();
        verification = in.readString();
        icon = in.readString();
        friendState = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(phoneName);
        dest.writeString(yjName);
        dest.writeString(yjNum);
        dest.writeString(phoneNum);
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

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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
}
