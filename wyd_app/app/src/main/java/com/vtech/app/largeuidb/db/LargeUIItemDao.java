package com.vtech.app.largeuidb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.vtech.app.largeuidb.largeuibean.LargeUIItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LARGE_UIITEM".
*/
public class LargeUIItemDao extends AbstractDao<LargeUIItem, Long> {

    public static final String TABLENAME = "LARGE_UIITEM";

    /**
     * Properties of entity LargeUIItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IsApp = new Property(1, boolean.class, "isApp", false, "IS_APP");
        public final static Property IsSystemApp = new Property(2, boolean.class, "isSystemApp", false, "IS_SYSTEM_APP");
        public final static Property IsMove = new Property(3, boolean.class, "isMove", false, "IS_MOVE");
        public final static Property IsModify = new Property(4, boolean.class, "isModify", false, "IS_MODIFY");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
        public final static Property ItemName = new Property(6, String.class, "itemName", false, "ITEM_NAME");
        public final static Property PackageName = new Property(7, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property Screen = new Property(8, int.class, "screen", false, "SCREEN");
        public final static Property CellX = new Property(9, int.class, "cellX", false, "CELL_X");
        public final static Property CellY = new Property(10, int.class, "cellY", false, "CELL_Y");
        public final static Property Container = new Property(11, Long.class, "Container", false, "CONTAINER");
    }


    public LargeUIItemDao(DaoConfig config) {
        super(config);
    }
    
    public LargeUIItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LARGE_UIITEM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"IS_APP\" INTEGER NOT NULL ," + // 1: isApp
                "\"IS_SYSTEM_APP\" INTEGER NOT NULL ," + // 2: isSystemApp
                "\"IS_MOVE\" INTEGER NOT NULL ," + // 3: isMove
                "\"IS_MODIFY\" INTEGER NOT NULL ," + // 4: isModify
                "\"TYPE\" INTEGER NOT NULL ," + // 5: type
                "\"ITEM_NAME\" TEXT," + // 6: itemName
                "\"PACKAGE_NAME\" TEXT," + // 7: packageName
                "\"SCREEN\" INTEGER NOT NULL ," + // 8: screen
                "\"CELL_X\" INTEGER NOT NULL ," + // 9: cellX
                "\"CELL_Y\" INTEGER NOT NULL ," + // 10: cellY
                "\"CONTAINER\" INTEGER);"); // 11: Container
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LARGE_UIITEM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LargeUIItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsApp() ? 1L: 0L);
        stmt.bindLong(3, entity.getIsSystemApp() ? 1L: 0L);
        stmt.bindLong(4, entity.getIsMove() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsModify() ? 1L: 0L);
        stmt.bindLong(6, entity.getType());
 
        String itemName = entity.getItemName();
        if (itemName != null) {
            stmt.bindString(7, itemName);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(8, packageName);
        }
        stmt.bindLong(9, entity.getScreen());
        stmt.bindLong(10, entity.getCellX());
        stmt.bindLong(11, entity.getCellY());
 
        Long Container = entity.getContainer();
        if (Container != null) {
            stmt.bindLong(12, Container);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LargeUIItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsApp() ? 1L: 0L);
        stmt.bindLong(3, entity.getIsSystemApp() ? 1L: 0L);
        stmt.bindLong(4, entity.getIsMove() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsModify() ? 1L: 0L);
        stmt.bindLong(6, entity.getType());
 
        String itemName = entity.getItemName();
        if (itemName != null) {
            stmt.bindString(7, itemName);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(8, packageName);
        }
        stmt.bindLong(9, entity.getScreen());
        stmt.bindLong(10, entity.getCellX());
        stmt.bindLong(11, entity.getCellY());
 
        Long Container = entity.getContainer();
        if (Container != null) {
            stmt.bindLong(12, Container);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LargeUIItem readEntity(Cursor cursor, int offset) {
        LargeUIItem entity = new LargeUIItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // isApp
            cursor.getShort(offset + 2) != 0, // isSystemApp
            cursor.getShort(offset + 3) != 0, // isMove
            cursor.getShort(offset + 4) != 0, // isModify
            cursor.getInt(offset + 5), // type
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // itemName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // packageName
            cursor.getInt(offset + 8), // screen
            cursor.getInt(offset + 9), // cellX
            cursor.getInt(offset + 10), // cellY
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11) // Container
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LargeUIItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsApp(cursor.getShort(offset + 1) != 0);
        entity.setIsSystemApp(cursor.getShort(offset + 2) != 0);
        entity.setIsMove(cursor.getShort(offset + 3) != 0);
        entity.setIsModify(cursor.getShort(offset + 4) != 0);
        entity.setType(cursor.getInt(offset + 5));
        entity.setItemName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPackageName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setScreen(cursor.getInt(offset + 8));
        entity.setCellX(cursor.getInt(offset + 9));
        entity.setCellY(cursor.getInt(offset + 10));
        entity.setContainer(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LargeUIItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LargeUIItem entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LargeUIItem entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}