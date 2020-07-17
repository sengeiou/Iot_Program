package com.vtech.app.moudle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.moudle.largeui.contacts.ContactBean;
import com.vtech.app.moudle.largeui.contacts.ContactBeanWithHead;
import com.vtech.app.moudle.largeui.contacts.ContactConstants;
import com.vtech.app.moudle.largeui.contacts.ContactPhotoUtils;
import java.util.ArrayList;
import java.util.List;

public class ContactsReceiver extends BroadcastReceiver {
    public static final String MODIFRY_CONTACT_DATA = "com.modify.contact.data";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MODIFRY_CONTACT_DATA)) {
            List<ContactBeanWithHead> cList = updateData(context);
            App.getInstance().setContactBeanWithHeadList(cList);
        }
    }

    private List<ContactBeanWithHead> updateData(Context context) {
        List<ContactBeanWithHead> contactBeanWithHeadList  = new ArrayList<>();
        String[] mContactsProjection = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts.STARRED,
                ContactConstants.PHONEBOOK_LABEL_PRIMARY};
        Cursor contactsCursor = context.getContentResolver().query(ContactConstants.PHONE_URI, mContactsProjection, null, null, ContactsContract.RawContacts.SORT_KEY_PRIMARY);
        if (contactsCursor != null) {
            parseCursor(context,contactsCursor,contactBeanWithHeadList);
        }
        return contactBeanWithHeadList;
    }

    private void parseCursor(Context context, Cursor contactsCursor, List<ContactBeanWithHead> contactBeanWithHeadList) {
        if (contactBeanWithHeadList.size() > 0) {
            contactBeanWithHeadList.clear();
        }
        String curHeadText = "";
        List<ContactBean> curHeadContactBeans = new ArrayList<>();
        List<ContactBeanWithHead> normalDatas = new ArrayList<>();
        List<ContactBean> contactStarredBeans = new ArrayList<>();
        while (contactsCursor.moveToNext()) {
            //获取联系人的id
            int _id = contactsCursor.getInt(0);
            String name = contactsCursor.getString(2);
            String sortValue = contactsCursor.getString(5);
            if (TextUtils.isEmpty(curHeadText)) {
                curHeadText = sortValue;
            }
            int starred = contactsCursor.getInt(4);
            String phone = contactsCursor.getString(1);
            Bitmap bitmap = ContactPhotoUtils.getPhoto(context.getContentResolver(), _id);
            Bitmap roundHeadImg = null;
            ContactBean bean;
            if (bitmap != null) {
                roundHeadImg = ContactPhotoUtils.getOvalBitmap(bitmap);
                bean = new ContactBean(name, phone, starred != 0, false, sortValue, _id, roundHeadImg);
            } else {
                bean = new ContactBean(name, phone, starred != 0, false, sortValue, _id, roundHeadImg);
            }
            if (!curHeadText.equals(sortValue)) {
                List<ContactBean> tempLisst = new ArrayList<>(curHeadContactBeans);
                normalDatas.add(new ContactBeanWithHead(curHeadText, tempLisst));
                curHeadText = sortValue;
                curHeadContactBeans.clear();
            }
            curHeadContactBeans.add(bean);

            if (bean.isStarred()) {
                contactStarredBeans.add(bean);
            }
        }
        if (curHeadContactBeans.size() > 0) {
            List<ContactBean> tempList = new ArrayList<>(curHeadContactBeans);
            normalDatas.add(new ContactBeanWithHead(curHeadText, tempList));
        }

        if (contactStarredBeans.size() > 0) {
            List<ContactBean> tempStarredList = new ArrayList<>(contactStarredBeans);
            if (tempStarredList.size() > 0) {
                contactBeanWithHeadList.add(new ContactBeanWithHead(context.getResources().getString(R.string.contact_collection), tempStarredList));
            }
        }
        if (normalDatas.size() > 0) {
            contactBeanWithHeadList.addAll(normalDatas);
        }
    }
}
