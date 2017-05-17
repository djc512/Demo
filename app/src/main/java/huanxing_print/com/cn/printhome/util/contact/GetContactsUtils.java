package huanxing_print.com.cn.printhome.util.contact;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;

/**
 * 获取联系人工具类
 * Created by wanghao on 2017/5/5.
 */

public class GetContactsUtils {
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
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if(null == cursor) {
            return infos;
        }

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(null == phones) {
                continue;
            }
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                PhoneContactInfo phoneContactInfo = new PhoneContactInfo();
                phoneContactInfo.setTelName(name);
                phoneContactInfo.setTelNo(phoneNumber.replace(" ",""));
                infos.add(phoneContactInfo);
            }
            phones.close();

//            /*
//             * 查找该联系人的email信息
//             */
//            Cursor emails = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            ArrayList<String> telEmails = null;
//            if (emails.getCount() > 0) {
//                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//                telEmails = new ArrayList<String>();
//            }
//            while (emails.moveToNext()) {
//                String email = emails.getString(emailIndex);
//                if (null != telEmails) {
//                    telEmails.add(email);
//                }
//            }

        }
        cursor.close();
        return infos;
    }

}
