package com.vtech.app.largeuidb.dbutils;

import android.content.Context;

import com.vtech.app.App;
import com.vtech.app.largeuidb.DaoManager;
import com.vtech.app.largeuidb.db.ContactsBeanDao;
import com.vtech.app.largeuidb.largeuibean.ContactsBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class ContactsUtil {
    private static final String TAG = ContactsUtil.class.getSimpleName();
    private DaoManager mManager;
    private Context mContext;

    public ContactsUtil() {
        mManager = DaoManager.getInstance(App.getInstance());
        this.mContext = App.getInstance();

    }

    public void insert(ContactsBean bean) {
        mManager.getDaoSession(mContext).getContactsBeanDao().insert(bean);
    }

    /**
     * 插入多条数据，在子线程操作
     */
    public boolean insertMulti(final List<ContactsBean> beanList) {
        boolean isSuccess = false;
        try {
            mManager.getDaoSession(mContext).runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ContactsBean bean : beanList) {
                        mManager.getDaoSession(mContext).insert(bean);
                    }
                }
            });
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteAll() {
        final List<ContactsBean> beanList = getAllItems();
        boolean isSuccess = false;
        try {
            mManager.getDaoSession(mContext).runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ContactsBean bean : beanList) {
                        mManager.getDaoSession(mContext).delete(bean);
                    }
                }
            });
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public List<ContactsBean> getAllItems() {
        QueryBuilder<ContactsBean> qb = mManager.getDaoSession(mContext).getContactsBeanDao().queryBuilder();
        List<ContactsBean> list = qb.list();
        return list;
    }

    public ContactsBean getItem(int type) {
        QueryBuilder<ContactsBean> qb = mManager.getDaoSession(mContext).getContactsBeanDao().queryBuilder();
        qb.where(ContactsBeanDao.Properties.Type.eq(type));
        List<ContactsBean> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public int getSize() {
        QueryBuilder<ContactsBean> qb = mManager.getDaoSession(mContext).getContactsBeanDao().queryBuilder();
        if (qb.list() != null) {
            return qb.list().size();
        }
        return 0;
    }
}
