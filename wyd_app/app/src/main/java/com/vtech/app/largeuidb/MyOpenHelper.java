package com.vtech.app.largeuidb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vtech.app.largeuidb.db.ContactsBeanDao;
import com.vtech.app.largeuidb.db.DaoMaster;
import com.vtech.app.largeuidb.db.LargeUIItemDao;

public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory) {
        super(paramContext, paramString, paramCursorFactory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.i("MyOpenHelper", "oldVersion: " + oldVersion + ", newVersion: " + newVersion);
        if (oldVersion < newVersion) {
            MigrationHelper.getInstance().migrate(db, LargeUIItemDao.class, ContactsBeanDao.class);
        }
    }

    /*  public void onUpgrade(Database database, int oldVersion, int newVersion) {
        super.onUpgrade(database, oldVersion, newVersion);
        //MigrationHelper.migrate(paramDatabase, new Class[] { LargeUIItemDao.class, ContactsBeanDao.class });
        Log.i("MyOpenHelper", "oldVersion: " + oldVersion + ", newVersion: " + newVersion);
        if (oldVersion < newVersion) {
            MigrationHelper.getInstance().migrate(database, LargeUIItemDao.class, ContactsBeanDao.class);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//             MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
        }

    }*/
}
