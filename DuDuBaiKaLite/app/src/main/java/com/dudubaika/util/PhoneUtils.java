package com.dudubaika.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class PhoneUtils {


    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION =
            new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /**
     * 获取手机联系人，优先从手机里面获取，如果获取不到再从SIM卡里面获取
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        List<HashMap<String, String>> allContactInfoOfPhone = getAllContactInfoOfPhone(context);
        if (allContactInfoOfPhone.size() == 0) {
            allContactInfoOfPhone = getAllContactInfoOfSIM(context);
        }
        return allContactInfoOfPhone;
    }


    /**
     * 获取手机联系人
     */
    public static List<HashMap<String, String>> getAllContactInfoOfPhone(Context context) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        ContentResolver resolver = context.getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    // 过滤字符串，只要数字
                    String phoneNumberF = RegexUtil.getNum(phoneNumber);

                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumberF))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                    HashMap<String, String> map = new HashMap<>();
                    contactName = SpannableUtils.filterCharToNormal(contactName);
                    contactName = contactName.replace("\n", "").replace("　", " ").trim();
                    map.put("name", contactName);
                    map.put("phone", phoneNumberF);
                    list.add(map);
                }
                phoneCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyComparator myComparator = new MyComparator();
        Collections.sort(list, myComparator);
        return list;
    }

    /**
     * 从SIM卡获取联系人
     */
    public static List<HashMap<String, String>> getAllContactInfoOfSIM(Context context) {
        //content://icc/adn
        //content://sim/adn
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Uri uri = Uri.parse("content://icc/adn");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            return list;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(Contacts.People._ID));
            String name = cursor.getString(cursor.getColumnIndex(Contacts.People.NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Contacts.People.NUMBER));
            name = SpannableUtils.filterCharToNormal(name);
            name = name.replace("\n", "").replace("　", " ").trim();
            // 过滤字符串，只要数字
            String phoneNumberF = RegexUtil.getNum(phoneNumber);

            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("phone", phoneNumberF);
            list.add(map);
        }
        MyComparator myComparator = new MyComparator();
        Collections.sort(list, myComparator);
        return list;
    }

}
