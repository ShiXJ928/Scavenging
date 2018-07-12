package com.zq.scavenging.application;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.zq.scavenging.gen.DaoMaster;
import com.zq.scavenging.gen.DaoSession;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by sxj on 2017/10/18.
 */
public class MyApplication extends MultiDexApplication {

    public static Context mContext;
    private List<Activity> mList = new LinkedList<>();
    private static MyApplication instance;
    private static DaoSession daoSession;

    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        App.initialize(this);

        //配置数据库
        setupDatabase();
    }

    public static Context getContext() {
        return mContext;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mList = new LinkedList<>();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.exit(0);
        }
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shop.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
