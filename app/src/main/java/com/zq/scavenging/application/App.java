package com.zq.scavenging.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zq.scavenging.util.Dimension;
import com.zq.scavenging.util.SharedUtility;

import java.io.File;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sxj on 2017/10/18.
 */
public class App {

    private static String temp_dir;  //保存图片的临时目录
    public static SharedUtility sharedUtility;
    public static ThreadPoolExecutor cachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

    public static void initialize(final Context context) {    //绑定context，用于一些初期事务
        initImageLoader(context);
        Dimension.init(context.getResources());
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        sharedUtility = getSharedUtility(context);
        temp_dir = getDiskDir(context) + File.separator + "ems" + File.separator + "tempPhoto" + File.separator;
        makeDirs(temp_dir);
    }

    public static SharedUtility getSharedUtility(Context ctx) {
        SharedUtility sharedUittly = new SharedUtility();
        sharedUittly.pref = ctx.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        sharedUittly.editor = sharedUittly.pref.edit();
        return sharedUittly;
    }

    public static void loginOut() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        sharedUtility.loginOut();
    }

    public static String getTemp_dir() {
        return temp_dir;
    }

    public static void makeDirs(String dir) {  //创建文件目录
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static void initImageLoader(Context context) {  //初始化imageloader
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
                .cacheInMemory(true).cacheOnDisc(true)
                .considerExifParams(true) // 调整图片方向
                .resetViewBeforeLoading(true) // 载入之前重置ImageView
//                .showImageOnLoading(R.mipmap.wait) // 载入时图片设置为黑色
//                .showImageOnFail(R.mipmap.error) // 加载失败时显示的图片
                .delayBeforeLoading(0) // 载入之前的延迟时间
                .cacheInMemory(true).cacheOnDisc(true)
                .build(); //
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(defaultDisplayImageOptions).memoryCacheExtraOptions(480, 800)
                .threadPoolSize(5).build();
        ImageLoader.getInstance().init(config);
    }

    @SuppressLint("NewApi")
    public static String getDiskDir(Context context) {  //当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径。
        String cachePath = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalFilesDir(null).getPath();
            } else {
                cachePath = context.getFilesDir().getPath();
            }
        } catch (Exception e) {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

}
