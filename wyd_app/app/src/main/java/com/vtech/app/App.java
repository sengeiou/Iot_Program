package com.vtech.app;

import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.moudle.largeui.contacts.ContactBean;
import com.vtech.app.moudle.largeui.contacts.ContactBeanWithHead;
import com.vtech.app.moudle.largeui.contacts.ContactConstants;
import com.vtech.app.moudle.largeui.contacts.ContactPhotoUtils;
import com.vtech.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static final String TAG = "App";
    private static String token = "";
    private int lang = -1;
    private List<ContactBeanWithHead> contactBeanWithHeadList;
    private List<AppInfo> appInfoList;
    private ContentResolver resolver;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        lang = Utils.getLangugeType(this);
        resolver = this.getContentResolver();
        contactBeanWithHeadList = updateData(resolver);
        appInfoList = getAppInfoList();
        saveContact();
        deleteContact();
        Log.i(TAG,"init onCreate");
    }

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getLang() {
        if (lang != -1)
            return lang;
        else
            return Utils.getLangugeType(this);
    }

    public List<AppInfo> getAppInfoList() {
        if(appInfoList != null)
            return appInfoList;
        else
            return appInfoList = getAllListApp(instance);
    }

    public void setAppInfoList(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
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
            Log.i(TAG, "all getAppProcessName: " + packageName + " , AppName : " + apps.get(i).activityInfo.loadLabel(packageManager).toString());
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

    public void setContactBeanWithHeadList(List<ContactBeanWithHead> contactBeanWithHeadList) {
        this.contactBeanWithHeadList = contactBeanWithHeadList;
    }

    public List<ContactBeanWithHead> getContactsList(){
        if(contactBeanWithHeadList != null)
            return contactBeanWithHeadList;
        else
            return contactBeanWithHeadList = updateData(resolver);
    }

    private List<ContactBeanWithHead> updateData(ContentResolver resolver) {
        List<ContactBeanWithHead> contactBeanWithHeadList  = new ArrayList<>();
        String[] mContactsProjection = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts.STARRED,
                ContactConstants.PHONEBOOK_LABEL_PRIMARY};
        Cursor contactsCursor = resolver.query(ContactConstants.PHONE_URI, mContactsProjection, null, null, ContactsContract.RawContacts.SORT_KEY_PRIMARY);
        if (contactsCursor != null) {
            parseCursor(contactsCursor,contactBeanWithHeadList);
        }
        return contactBeanWithHeadList;
    }

    private void parseCursor(Cursor contactsCursor,List<ContactBeanWithHead> contactBeanWithHeadList) {
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
            Bitmap bitmap = ContactPhotoUtils.getPhoto(resolver, _id);
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
                contactBeanWithHeadList.add(new ContactBeanWithHead(this.getResources().getString(R.string.contact_collection), tempStarredList));
            }
        }
        if (normalDatas.size() > 0) {
            contactBeanWithHeadList.addAll(normalDatas);
        }
    }

    private void saveContact() {
        ContentValues values = new ContentValues();
        Uri insert = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long raw_id = ContentUris.parseId(insert);
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        //name
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, raw_id)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "123rong")
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,null)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,null)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,null)
                .build());
        //number
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, raw_id)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "123")
                .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void deleteContact() {
        String name = "123rong";
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String id = getContactID(name);
        ops.add(ContentProviderOperation
                .newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{String.valueOf(id)})
                .build());
        ops.add(ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{String.valueOf(id)})
                .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContactID(String name) {
        String id = "";
        Cursor cursor = getContentResolver().query(
                android.provider.ContactsContract.Contacts.CONTENT_URI,
                new String[]{android.provider.ContactsContract.Contacts._ID},
                android.provider.ContactsContract.Contacts.DISPLAY_NAME +
                        "='" + name + "'", null, null);
        if (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(
                    android.provider.ContactsContract.Contacts._ID));
        }
        return id;
    }
}
