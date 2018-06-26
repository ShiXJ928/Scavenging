package com.zq.scavenging.application;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by sxj on 2017/10/18.
 */
public class MyApplication extends MultiDexApplication {

    public static Context mContext;
    private List<Activity> mList = new LinkedList<>();
    private static MyApplication instance;

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
}
