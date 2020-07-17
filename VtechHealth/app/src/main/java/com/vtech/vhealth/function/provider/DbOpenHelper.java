package com.vtech.vhealth.function.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/****
 * @author jason
 * 数据库辅助类
 * */
public class DbOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库的版本号，以后要升级数据库，修改版本号为 +1 即可
     */
    private static final int DATABASE_VERSION = 2;

    private volatile static DbOpenHelper instance;

    public static final String TAG = "DbOpenHelper";
    private static final String DB_NAME = "serial_provider.db";
    public static final String HEALTH_TABLE_NAME = "health";
    private static final int DB_VERSION = 1;

    public static final String HEALTH_ISBP = "isbp";
    public static final String HEALTH_IDBP = "idbp";
    public static final String HEALTH_IHRATE = "ihrate";

    public static final String HEALTH_IHRATE_MAX = "ihrate_max";
    public static final String HEALTH_IHRATE_MIN = "ihrate_min";
    public static final String HEALTH_IHRATE_AVG = "ihrate_avg";

    public static final String HEALTH_ISBP_MAX = "isbp_max";
    public static final String HEALTH_ISBP_MIN = "isbp_min";
    public static final String HEALTH_ISBP_AVG = "isbp_avg";

    public static final String HEALTH_IDBP_MAX = "idbp_max";
    public static final String HEALTH_IDBP_MIN = "idbp_min";
    public static final String HEALTH_IDBP_AVG = "idbp_avg";


    public static final String HEALTH_AISBP = "aisbp";
    public static final String HEALTH_AIDBP = "aidbp";
    public static final String HEALTH_AIHRATE = "aihrate";
    //数据库数据状态，默认0 未上传，1为成功上传服务器
    public static final String HEALTH_STATUS = "status";
    public static final String HEALTH_CREATETIME = "createTime";
    public static final String HEALTH_USERID = "userId";
    public static final String HEALTH_DEVICESID = "devicesId";
    public static final String HEALTH_ID = "id";
    public static final String HEALTH_DAY = "day";

//   ilsbp  收缩压 高压   aisbp 平均值
//   idbp   舒张压 低压   aidbp 平均值
//   ihrate  心率        aihrate 平均值
//   userId 用户ID 设备id


    private String mCreateTableSql = "create table if not exists " + HEALTH_TABLE_NAME + "(id integer primary key AUTOINCREMENT ," + HEALTH_CREATETIME + " TEXT, " +
            HEALTH_USERID + " TEXT, " + HEALTH_DEVICESID + " TEXT, " + HEALTH_ISBP + " integer," + HEALTH_IDBP + "  integer," + HEALTH_IHRATE + "   integer,"
            + HEALTH_AISBP + " integer," + HEALTH_AIDBP + " integer," + HEALTH_AIHRATE + " integer," + HEALTH_STATUS + " integer)";


    private DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private DbOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "DbOpenHelper onCreate");
        db.execSQL(mCreateTableSql);
    }


    /**
     * 单例模式
     *
     * @param context 传入上下文
     * @return 返回MySQLiteOpenHelper对象
     */
    public static DbOpenHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (DbOpenHelper.class) {
                if (null == instance) {
                    instance = new DbOpenHelper(context, DB_NAME, null, DATABASE_VERSION);
                }
            }
        }
        return instance;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


