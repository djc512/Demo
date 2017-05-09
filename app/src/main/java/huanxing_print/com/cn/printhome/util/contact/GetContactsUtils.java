package huanxing_print.com.cn.printhome.util.contact;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;

/**
 * 获取联系人工具类
 * Created by wanghao on 2017/5/5.
 */

public class GetContactsUtils {
    private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };
    private Context mContext;

    public GetContactsUtils(Context context) {
        mContext = context;

    }

    /**
     * 获取系统联系人信息
     *
     * @return
     */
    public List<PhoneContactInfo> getSystemContactInfos() {

        List<PhoneContactInfo> infos = new ArrayList<PhoneContactInfo>();
        Cursor cursor = mContext.getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (cursor != null) {

            while (cursor.moveToNext()) {

                PhoneContactInfo info = new PhoneContactInfo();
                String contactName = cursor.getString(0);
                String phoneNumber = cursor.getString(1);
                info.setTelName(contactName);
                info.setTelNo(phoneNumber.replace(" ",""));
                infos.add(info);
                info = null;
            }
            cursor.close();

        }
        return infos;
    }

    /**
     * 分页查询系统联系人信息
     *
     * @param first
     *            起始位置
     * @param max
     *            最大值
     * @return
     */
    public List<PhoneContactInfo> getContactsByPage(int first, int max) {

        List<PhoneContactInfo> infos = new ArrayList<PhoneContactInfo>();
        Cursor cursor = mContext.getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null,
                "_id  limit " + first + "," + max);
        if (cursor != null) {

            while (cursor.moveToNext()) {

                PhoneContactInfo info = new PhoneContactInfo();
                String contactName = cursor.getString(0);
                String phoneNumber = cursor.getString(1);
                info.setTelName(contactName);
                info.setTelNo(phoneNumber);
                infos.add(info);
                info = null;
            }
            cursor.close();

        }
        return infos;
    }

    /**
     * 获得系统联系人的所有记录数目
     *
     * @return
     */
    public int getAllCounts() {
        int num = 0;
        // 使用ContentResolver查找联系人数据
        Cursor cursor = mContext.getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        num = cursor.getCount();
        cursor.close();

        return num;
    }
}
