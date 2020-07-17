
注册一个广播接收器

public class StartReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
             Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.vtech.check", "com.vtech.check.activity.DisplayScreenActivity");
            try {
                 intent.setComponent(cn);
                           startActivity(intent);
            } catch (Exception e) {
             //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
             }
        }
    }
}


        <receiver android:name=".StartReceiver">
            <intent-filter>
                <action android:name="android.provider.Telephony.SECRET_CODE" />
                <data android:scheme="android_secret_code" android:host="83789" />
            </intent-filter>
        </receiver>

