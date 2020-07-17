package com.vtech.app.moudle.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.moudle.largeui.contacts.ContactBean;
import com.vtech.app.moudle.largeui.contacts.ContactBeanWithHead;
import com.vtech.app.moudle.largeui.contacts.ContactConstants;
import com.vtech.app.moudle.largeui.contacts.ContactPhotoUtils;
import com.vtech.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class LocaleChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            List<AppInfo> appLists = getAllListApp(context);
            App.getInstance().setAppInfoList(appLists);

            List<ContactBeanWithHead> cList = updateData(context);
            App.getInstance().setContactBeanWithHeadList(cList);

            int lang = Utils.getLangugeType(context.getApplicationContext());
            App.getInstance().setLang(lang);
        }
    }

    private List<AppInfo> getAllListApp(Context context) {
        List<AppInfo> mDataList = new ArrayList<>();
        final PackageManager packageManager = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < apps.size(); i++) {
            String packageName = apps.get(i).activityInfo.packageName;
            if (packageName.contains("com.android.email")
                    || packageName.contains("com.sohu.inputmethod.sogou")
                    || packageName.contains("com.vtech.voiceassistant")
                    || packageName.contains("com.android.quicksearchbox")
                    || packageName.contains("org.chromium.webview_shell")
                    || packageName.contains("com.softwinner.fireplayer")
                    || packageName.contains("com.iflytek.inputmethod")
                    || packageName.contains("com.android.dialer")
                    || packageName.contains("com.android.messaging")
                    || packageName.contains("com.android.settings")
                    || packageName.contains("com.vtech.vhealth")
                    || packageName.contains("com.vtech.homesecurity")
                    || packageName.contains("com.whatsapp")
                    || packageName.contains("com.tencent.mm")
                    || packageName.contains("com.vtech.app") //移除自身
                    || packageName.contains("com.android.contacts")//移除联系人
                    || packageName.contains("com.vtech.schedule")
                    || packageName.contains("com.vtech.face")) {
                continue;
            }
            AppInfo bean = new AppInfo();
            bean.setAppName(apps.get(i).activityInfo.loadLabel(packageManager).toString());
            bean.setPackageName(packageName);
            if (packageName.contains("com.android.settings")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_setting));
            } else if (packageName.contains("com.android.camera2")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_camera));
            } else if (packageName.contains("com.softwinner.update")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_update));
            } else if (packageName.contains("com.android.gallery3d")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_gallery));
            } else if (packageName.contains("com.android.contacts")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_contact));
            } else if (packageName.contains("com.android.deskclock")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_deskclock));
            } else if (packageName.contains("com.android.messaging")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_messaging));
            } else if (packageName.contains("com.android.calculator2")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_calculator));
            } else if (packageName.contains("com.android.soundrecorder")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_soundrecorder));
            } else if (packageName.contains("com.android.documentsui")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_documentsui));
            } else {
                bean.setDrawable(apps.get(i).activityInfo.loadIcon(packageManager));
            }
            mDataList.add(bean);
        }
        return mDataList;
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
                contactBeanWithHeadList.add(new ContactBeanWithHead(context.getApplicationContext().getResources().getString(R.string.contact_collection), tempStarredList));
            }
        }
        if (normalDatas.size() > 0) {
            contactBeanWithHeadList.addAll(normalDatas);
        }
    }
}
