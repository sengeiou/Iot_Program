package com.vtech.app.largeuidb.dbutils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.DaoManager;
import com.vtech.app.largeuidb.db.LargeUIItemDao;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private static final String TAG = ItemUtil.class.getSimpleName();
    private DaoManager mManager;
    private Context context;

    public ItemUtil(Context context) {
        this.context = context;
        mManager = DaoManager.getInstance(context);
    }

    public void insert(LargeUIItem item) {
        List<LargeUIItem> list = getAllItems();
        for (LargeUIItem largeUIItem : list) {
            if (largeUIItem.getPackageName().equals(item.getPackageName())) {
                Toast.makeText(context, context.getResources().getString(R.string.re_add_application), Toast.LENGTH_LONG).show();
                return;
            }
        }
        long id = mManager.getDaoSession(context).getLargeUIItemDao().insert(item);
        if (id >= 0) {
            notifyDataChanged();
        }
        if (item.getCellX() == (Constants.PAGE_APP_MAX_COUNT / 2 - 1) && item.getCellY() == (Constants.PAGE_COLUMN - 1)) {
            notifyPageDataChanged();
        }
    }

    /**
     * 插入多条数据，在子线程操作
     */
    public boolean insertMulti(final List<LargeUIItem> itemList) {
        Log.i(TAG, " insert resort size is  " + itemList.size());
        boolean isSuccess = false;
        try {
            mManager.getDaoSession(context).runInTx(new Runnable() {
                @Override
                public void run() {
                    for (LargeUIItem item : itemList) {
                        mManager.getDaoSession(context).insert(item);
                        if (item.getCellX() == (Constants.PAGE_APP_MAX_COUNT / 2 - 1) && item.getCellY() == (Constants.PAGE_COLUMN - 1)) {
                            notifyPageDataChanged();
                        }

                    }
                }
            });
            isSuccess = true;
            notifyDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public void update(LargeUIItem item) {
        mManager.getDaoSession(context).update(item);
        notifyDataChanged();
    }

    public void delete(LargeUIItem item) {
        mManager.getDaoSession(context).delete(item);
        int screen = item.getScreen();
        int new_last_screen = delete_resort();
        Log.d(TAG, "删除的图标信息为: " + item.toString());
        //获取当前屏幕是否还存在应用图标，如果存在，则更新当前fragment 否则跳转到上一页
        if ((getListByScreenNum(screen) != null && getListByScreenNum(screen).size() != 0)) {
            if (getListByScreenNum(screen + 1) == null || getListByScreenNum(screen + 1).size() == 0) { //如果下一页屏幕没有应用列表列则更新页面
                Log.d(TAG, " 如果下一页不存在应用列表，则全部更新");
                notifyPageDataChangedL(new_last_screen);
                return;
            }
            notifyDataChanged();
        } else {
            notifyPageDataChangedL(new_last_screen);
        }
    }

    /**
     * 删除数据需要重新排列数据库
     *
     * @return 最多显示的页卡值
     */
    private int delete_resort() {
        int last_screen = 1;
        final List<LargeUIItem> olderlist = getAllItems();
        List<LargeUIItem> new_itemList = new ArrayList<>();
        int cellX;
        int cellY;
        int screen;
        for (int i = 0; i < olderlist.size(); i++) {
            if (i < Constants.PAGE_APP_MAX_COUNT) {
                cellX = i / 2;
            } else {
                cellX = (i - Constants.PAGE_APP_MAX_COUNT) / 2;
            }
            cellY = i % 2;
            screen = i / Constants.PAGE_APP_MAX_COUNT;
            last_screen = screen;
            LargeUIItem item0 = new LargeUIItem();
            item0.setType(i);
            item0.setItemName(olderlist.get(i).getItemName());
            item0.setScreen(screen);
            item0.setCellX(cellX);
            item0.setCellY(cellY);
            item0.setIsMove(olderlist.get(i).getIsMove());
            item0.setIsModify(olderlist.get(i).getIsModify());
            //Logger.d(TAG, "initLargeUIData, name: " + default_add_app_name[i] + ", screen: " + screen + "(" + cellX + "," + cellY + ")");
            String pkgName = olderlist.get(i).getPackageName();
            Log.d(TAG, "resort, pkgName: " + pkgName);
            item0.setPackageName(pkgName);
            if (!TextUtils.isEmpty(pkgName)) {
                item0.setIsApp(true);
            } else {
                item0.setIsApp(false);
            }
            new_itemList.add(item0);
        }
        deleteAll();
        final List<LargeUIItem> itemList = new_itemList;
        try {
            mManager.getDaoSession(context).runInTx(new Runnable() {
                @Override
                public void run() {
                    for (LargeUIItem item : itemList) {
                        mManager.getDaoSession(context).insert(item);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, " after delete and resort new last screen is " + last_screen);
        return last_screen;
    }

    public boolean deleteAll() {
        //final List<LargeUIItem> itemList = getAllItems();
        boolean isSuccess = false;
        try {
            mManager.getDaoSession(context).runInTx(new Runnable() {
                @Override
                public void run() {
//                    for (LargeUIItem item : itemList) {
//                        mManager.getDaoSession(context).delete(item);
//
//                    }
                    mManager.getDaoSession(context).deleteAll(LargeUIItem.class);
                }
            });
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public List<LargeUIItem> getAllItems() {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        List<LargeUIItem> list = qb.list();
        return list;
    }

    public LargeUIItem getItem(int type) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Type.eq(type));
        List<LargeUIItem> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public List<LargeUIItem> getContainerItem(Long containerId) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Container.eq(containerId));
        List<LargeUIItem> list = qb.list();
        return list;
    }

    public int getContainerItemSize(Long containerId) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Container.eq(containerId));
        List<LargeUIItem> list = getContainerItem(containerId);
        if (qb.list() != null) {
            return qb.list().size();
        }
        return 0;
    }

    public LargeUIItem getItemByCoordinate(int screen, int cellX, int cellY) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Screen.eq(screen),
                LargeUIItemDao.Properties.CellX.eq(cellX),
                LargeUIItemDao.Properties.CellY.eq(cellY));
        List<LargeUIItem> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public int getScreenSize(int screen) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Screen.eq(screen));
        for (LargeUIItem uiItem : qb.where(LargeUIItemDao.Properties.Screen.eq(screen)).build().list()) {
            Log.d(TAG, "dddd aaaa ddd ad" + uiItem.toString());
        }
        if (qb.list() != null) {
            return qb.list().size();
        }
        return 0;
    }

    /**
     * add by tuliyuan for add get list from screen num
     *
     * @return
     */
    public List<LargeUIItem> getListByScreenNum(int screen) {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        qb.where(LargeUIItemDao.Properties.Screen.eq(screen));
        // Log.d(TAG, "dddd aaaa ddd ad" + qb.where(LargeUIItemDao.Properties.Screen.eq(screen)).build().list().size());
        if (qb.where(LargeUIItemDao.Properties.Screen.eq(screen)).build().list() != null) {
            return qb.where(LargeUIItemDao.Properties.Screen.eq(screen)).build().list();
        }
        return null;
    }


    public int getSize() {
        QueryBuilder<LargeUIItem> qb = mManager.getDaoSession(context).getLargeUIItemDao().queryBuilder();
        if (qb.list() != null) {
            return qb.list().size();
        }
        return 0;
    }

    private void notifyDataChanged() {
        Log.d("ItemUtil", "notifyDataChanged");
        Intent intent = new Intent(Constants.APP_ITEM_COUNT_CHANGED_ACTION);
        intent.setPackage(App.getInstance().getPackageName());
        context.sendBroadcast(intent);
    }

    private void notifyPageDataChanged() {
        Log.d("ItemUtil", "notifyPageDataChanged");
        Intent intent = new Intent(Constants.APP_ITEM_COUNT_PAGE_CHANGED_ACTION);
        intent.setPackage(App.getInstance().getPackageName());
        context.sendBroadcast(intent);
    }

    private void notifyPageDataChangedL(int screen) {
        Log.d("ItemUtil", "notifyPageDataChangedL");
        Intent intent = new Intent(Constants.APP_ITEM_COUNT_PAGE_CHANGED_ACTION);
        intent.putExtra("screen_page", screen);
        intent.setPackage(App.getInstance().getPackageName());
        context.sendBroadcast(intent);
    }
}
