package com.zq.scavenging.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/5/9.
 */

public class Utility {
    /**
     * 验证手机格式
     */
    public static boolean isEmail(String email) {
        String num = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return email.matches(num);
        }
    }

    public static void displayRoundImage(String url, ImageView imageview, int rDP, int photo) {   //设置圆形头像 第三个参数为imageview的一半数值
        DisplayImageOptions image_loader_options = new DisplayImageOptions.Builder()
                .showStubImage(photo)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(photo)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(photo)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                            // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(Dimension.dp2px(rDP)))    // 设置成圆角图片
                .build();                                    // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(url, imageview, image_loader_options, new AnimateFirstDisplayListener());
    }

    public static void displayImage(String url, ImageView imageview, int photo) {   //设置圆形头像 第三个参数为imageview的一半数值
        DisplayImageOptions image_loader_options = new DisplayImageOptions.Builder()
                .showStubImage(photo)            // 设置图片下载期间显示的图片
                .showImageForEmptyUri(photo)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(photo)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                            // 设置下载的图片是否缓存在SD卡中
                .build();                                    // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(url, imageview, image_loader_options, new AnimateFirstDisplayListener());
    }

    public static BitmapDrawable getBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);

        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
