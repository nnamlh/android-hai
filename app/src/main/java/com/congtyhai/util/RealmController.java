package com.congtyhai.util;

import com.congtyhai.model.data.DCheckIn;
import com.congtyhai.model.data.DHistoryProductScan;
import com.congtyhai.model.data.DMsgToHai;
import com.congtyhai.model.data.NotificationInfo;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by NAM on 11/2/2016.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {

        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public long getNextKey(Class object)
    {
        try {
            Number max = realm.where(object).max("id");
            if(max == null) {
                return  1;
            } else {
                return max.intValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e)
        { return 0; }
    }


    //clear all objects from Book.class
    public void clearNotificationAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(NotificationInfo.class);
            }
        });
    }

    //clear all objects from Book.class
    public void clearCheckInAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(DCheckIn.class);
            }
        });
    }

    public void clearMsgToHai() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(DMsgToHai.class);
            }
        });
    }

    public void clearAll(final Class object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(object);
            }

        });
    }
    public void clearAllData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }

        });
    }

    //find all objects in the Book.class
    public RealmResults<NotificationInfo> getNotificaitons() {

        return realm.where(NotificationInfo.class).findAllSorted("id", Sort.DESCENDING);
    }

    //find all objects in the Book.class
    public RealmResults<DCheckIn> getCheckIns() {

        return realm.where(DCheckIn.class).findAll();
    }


    public RealmResults<DMsgToHai> getAllMsg() {

        return realm.where(DMsgToHai.class).findAll();
    }

    //HistoryProductScan
    public RealmResults<DHistoryProductScan> getHistoryProductScan() {

        return realm.where(DHistoryProductScan.class).findAll();
    }
    public DHistoryProductScan getHistoryProductScan (long id) {

        return realm.where(DHistoryProductScan.class).equalTo("id", id).findFirst();
    }

    //query a single item with the given id
    public NotificationInfo getNotification(String id) {

        return realm.where(NotificationInfo.class).equalTo("id", id).findFirst();
    }

    public DCheckIn getCheckIn(long id) {

        return realm.where(DCheckIn.class).equalTo("id", id).findFirst();
    }


    //query example
    public RealmResults<NotificationInfo> queryedNotification() {

        return realm.where(NotificationInfo.class)
                .equalTo("read", 0).findAll();

    }

    public DHistoryProductScan queryHistoryProductScan(long id) {

        return realm.where(DHistoryProductScan.class)
                .equalTo("id", id).findFirst();

    }
}
