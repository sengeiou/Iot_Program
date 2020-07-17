package com.vtech.app.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.github.promeg.pinyinhelper.Pinyin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chengzj
 * @date 2018/10/9 19:27
 */
public class Utils {
    private static final String TAG = "Util";

    /**
     * 判断列表第一个是否显示
     *
     * @param listView
     * @return
     */
    public static boolean isFirstItemVisible(AbsListView listView) {
        Adapter adapter = listView.getAdapter();
        if (null != adapter && !adapter.isEmpty()) {
            int mostTop = listView.getChildCount() > 0 ? listView.getChildAt(0).getTop() : 0;
            return mostTop >= 0;
        } else {
            return true;
        }
    }

    /**
     * 设置listview高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除   listView.setLayoutParams(params); }
    }


    /**
     * 计算ListView宽高
     *
     * @param listView
     */
    public static int calListViewWidthAndHeight(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Log.d("数据" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        Log.d("数据", "listview总高度=" + params.height);
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }

    /**
     * 计算GridView宽高
     *
     * @param gridView
     */
    public static void calGridViewWidthAndHeight(int numColumns, GridView gridView) {

        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高

            if ((i + 1) % numColumns == 0) {
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }

            if ((i + 1) == len && (i + 1) % numColumns != 0) {
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }

        totalHeight += 40;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        gridView.setLayoutParams(params);
    }


    /**
     * 将bitmap转化为二进制
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将dp转化为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    /**
     * 设置全屏幕覆盖状态栏
     *
     * @param mPopupWindow
     * @param needFullScreen
     */
    public static void fitPopupWindowOverStatusBar(PopupWindow mPopupWindow, boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(needFullScreen);
                mLayoutInScreen.set(mPopupWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开系统联系人
     *
     * @param fragment
     */
    public static void openContact(Fragment fragment) {
        //检查是否有读取联系人权限
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        fragment.startActivityForResult(intent, 1);
    }

    /**
     * 去掉手机号内除数字外的所有字符
     *
     * @param phoneNum 手机号
     * @return
     */
    public static String formatPhoneNum(String phoneNum) {
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }

    /**
     * 获取农历
     *
     * @return
     */
    public static String getNongLi() {
        Calendar calendar = Calendar.getInstance();
        Lunar lunar = new Lunar(calendar);
        return lunar.getDayString();
    }

    /**
     * 读取联系人信息
     *
     * @param context
     * @param uri
     * @return
     */
    public static String[] getPhoneContacts(Context context, Uri uri) {
        String[] contact = new String[3];
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            contact[1] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact[2] = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
//            contact[2] = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTENT_URI));
            Log.i("contacts", contact[0]);
            Log.i("contactsUsername", contact[1]);
            Log.i("contact_id", contact[2]);

//            getContactHeadImg(context,contact[2]);
            cursor.close();
        }
        return contact;
    }

    /**
     * 获取联系人图片
     *
     * @param context
     * @param contact_id
     * @return
     */
    public static Bitmap getContactHeadImg(Context context, String contact_id) {
        ContentResolver cr = context.getContentResolver();
        Uri url = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                Long.parseLong(contact_id));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, url);
        Bitmap photo = BitmapFactory.decodeStream(input);
        return createCircleImage(photo);
    }

    /**
     * 裁减图片为圆形
     *
     * @param source
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) return null;
        int width = source.getWidth();
        int height = source.getHeight();
        float raduis = Math.min(width, height) * 0.5f;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setColor(Color.RED);
        //画布设置遮罩效果
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        //处理图像数据
        Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        //bitmap的显示由画笔paint来决定
        canvas.drawCircle(width * 0.5f, height * 0.5f, raduis, paint);
        return bitmap;
    }

    /**
     * 获取是否是第一次开机
     *
     * @param context
     * @return
     */
    public static boolean getIsFirstBootStates(Context context) {
        boolean isFirst = PreferenceUtils.getPrefBoolean(context, PreferenceConstants.IS_FIRST_BOOT, true);
        if (isFirst) {
            PreferenceUtils.setPrefBoolean(context, PreferenceConstants.IS_FIRST_BOOT, false);
        }
        return isFirst;
    }

    /**
     * 获取当前设备的IMIE，需与上面的isPhone一起使用
     * 需添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (isPhone(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("getDeviceIMEI", "没有相应权限");
                return "";
            }
            deviceId = tm.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = tm.getSimSerialNumber();
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUUID(context);
        }

        Log.e("getDeviceIMEI", "deviceId : " + deviceId);
        return deviceId;
    }

    /**
     * 判断设备是否是手机
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 得到全局唯一UUID
     *
     * @return
     */
    public static String getUUID(Context context) {
        String uuid = "";
        SharedPreferences mShare = context.getSharedPreferences("uuid", context.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }

    private static final String AUTHORITY = "com.vtech.voiceassistant.comprovider";
    private static Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CASE_GET_COM = "getComdata";
    public static final String KEY_COM_PROVIDER = "comprovider";

    public static String getDeviceId(Context context) {
        try {
            Bundle bundle = context.getContentResolver().call(URI, CASE_GET_COM, "", null);
            if (bundle != null) {
                String comDataStr = bundle.getString(KEY_COM_PROVIDER);
                Log.i(TAG, "getDeviceId data : " + comDataStr);
                if (!TextUtils.isEmpty(comDataStr)) {
//                mComData = JsonUtil.jsonToBean(comDataStr, ComData.class);
                    try {
                        JSONObject jsonObject = new JSONObject(comDataStr);
                        String deviceId = jsonObject.optString("deviceId");
                        Log.i(TAG, "getDeviceId deviceId : " + deviceId);

                        return deviceId;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 启动微信app
     *
     * @param context
     */
    public static void startWx(Context context) {
        if (!isWeixinAvilible(context)) {
            ToastUtils.showShort(context, "请先安装微信");
            return;
        }
        Intent lan = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(lan.getComponent());
        context.startActivity(intent);
    }

    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取系统语言
     * @param context
     * @return
     */
    public static String getSystemLanguge(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        Log.i(TAG, "getSystemLanguge lang-country : " + lang);

        return lang;
    }

    /**
     * 获取语言类别：  0简体 1翻译 2英文 必须填
     * @param context
     * @return
     */
    public static int getLangugeType(Context context) {
        String lang = getSystemLanguge(context);
        if (TextUtils.isEmpty(lang)) {
            return 0;
        }
        if("zh-CN".equals(lang)){
            return 0;
        }else if("zh-HK".equals(lang)){
            return 1;
        }else if(lang.startsWith("en")){
            return 2;
        }
        return 0;
    }

    public static final String TOKEN_AUTHORITY = "com.vtech.app.homeprovider";
    public static final String CMD_GET_TOKEN = "cmd_get_token";
    public static final String TOKEN = "token";

    public static String getToken(Context context){
        String token = "";
        Uri uri = Uri.parse("content://com.vtech.app.homeprovider");
        Bundle bundle = context.getContentResolver().call(uri, CMD_GET_TOKEN, "", null);
        if (bundle != null) {
             token = bundle.getString(TOKEN);
        }
        return token;
    }


    public static String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)).toUpperCase());
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(new Random().nextInt(1000));

            System.out.println(new Random().nextInt());
        }
    }

}
