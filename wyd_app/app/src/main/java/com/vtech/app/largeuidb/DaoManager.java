package com.vtech.app.largeuidb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vtech.app.App;
import com.vtech.app.largeuidb.db.DaoMaster;
import com.vtech.app.largeuidb.db.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

public class DaoManager {
    private static final String TAG = DaoManager.class.getSimpleName();
    private static final String DB_NAME = "largeui-db";

    //多线程中要被共享的使用volatile关键字修饰
    private volatile static DaoManager manager;
    private static DaoMaster sDaoMaster;
    private static MyOpenHelper sHelper;
    private static DaoSession sDaoSession;


    //private static DaoManager mDbManager;
    //private static DaoMaster.DevOpenHelper mDevOpenHelper;
    //private static DaoMaster mDaoMaster;
    //private static DaoSession mDaoSession;

    private DaoManager(Context context) {
        //sHelper = new DaoMaster.DevOpenHelper(App.getInstance(), DB_NAME);
        sHelper = new MyOpenHelper(context, DB_NAME,null);
        getDaoMaster(context);
        getDaoSession(context);
    }

    public static DaoManager getInstance(Context context) {
        if (null == manager) {
            synchronized (DaoManager.class) {
                if (null == manager) {
                    manager = new DaoManager(context);
                }
            }
        }
        return manager;
    }



    /**
     * 获取可读数据库
     *
     * @return
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == sHelper) {
            getInstance(context);
        }
        return sHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == sHelper) {
            getInstance(context);
        }

        return sHelper.getWritableDatabase();
    }

    /**
     * 获取DaoMaster
     *
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (null == sDaoMaster) {
            synchronized (DaoManager.class) {
                if (null == sDaoMaster) {
                    sDaoMaster = new DaoMaster(getWritableDatabase(context));
                }
            }
        }
        return sDaoMaster;
    }

    /**
     * 获取DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (null == sDaoSession) {
            synchronized (DaoManager.class) {
                sDaoSession = getDaoMaster(context).newSession();
            }
        }
        return sDaoSession;
    }

}