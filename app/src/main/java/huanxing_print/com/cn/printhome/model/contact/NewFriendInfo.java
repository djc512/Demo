package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendInfo implements Parcelable {
    private int type;//用于界面显示item
    private String name;
    private String verification;
    private String iconPath;
    private int friendState;

    public enum STATE{
        NORMAL,AGREE,WAIT
    }

    public NewFriendInfo() {

    }

    protected NewFriendInfo(Parcel in) {
        type = in.readInt();
        name = in.readString();
        verification = in.readString();
        iconPath = in.readString();
        friendState = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(verification);
        dest.writeString(iconPath);
        dest.writeInt(friendState);
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getFriendState() {
        return friendState;
    }

    public void setFriendState(int friendState) {
        this.friendState = friendState;
    }
}
