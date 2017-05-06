package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghao on 2017/5/2.
 */

public class ContactInfo implements Parcelable{
    private String name;
    private String yjNum;
    private String iconPath;
    private boolean isFriend;
    private boolean isAddRequest;
    private int type;//用于联系人页面item类型

    public ContactInfo(){

    }

    protected ContactInfo(Parcel in) {
        name = in.readString();
        yjNum = in.readString();
        iconPath = in.readString();
        isFriend = in.readByte() != 0;
        isAddRequest = in.readByte() != 0;
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(yjNum);
        dest.writeString(iconPath);
        dest.writeByte((byte) (isFriend ? 1 : 0));
        dest.writeByte((byte) (isAddRequest ? 1 : 0));
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel in) {
            return new ContactInfo(in);
        }

        @Override
        public ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getYjNum() {
        return yjNum;
    }

    public void setYjNum(String yjNum) {
        this.yjNum = yjNum;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isAddRequest() {
        return isAddRequest;
    }

    public void setAddRequest(boolean addRequest) {
        isAddRequest = addRequest;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
