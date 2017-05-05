package huanxing_print.com.cn.printhome.model.contact;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupInfo implements Parcelable{
    private String groupIcon;
    private String groupName;
    private ArrayList<ContactInfo> members;

    public GroupInfo() {

    }

    protected GroupInfo(Parcel in) {
        groupIcon = in.readString();
        groupName = in.readString();
        members = in.createTypedArrayList(ContactInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupIcon);
        dest.writeString(groupName);
        dest.writeTypedList(members);
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

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<ContactInfo> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<ContactInfo> members) {
        this.members = members;
    }
}
