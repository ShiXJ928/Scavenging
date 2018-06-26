package com.zq.scavenging.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Dimension {
    private static float density;
    private static int screen_width;
    private static int screen_height;
    private static DisplayMetrics display;

    public static void init(Resources res) {
        display = res.getDisplayMetrics();
        setDensity(display.density);
        screen_width = display.widthPixels;
        screen_height = display.heightPixels;
    }

    public static int getScreen_width() {
        return screen_width;
    }

    public static int getScreen_height() {
        return screen_height;
    }

    public static float getDensity() {
        return density;
    }

    public static void setDensity(float d) {
        density = d;
    }

    public static int px2dp(int px) {
        return (int) Math.ceil(px / density);
    }

    public static int dp2px(int dp) {
        return (int) Math.ceil(dp * density);
    }

    public static int sp2px(float spValue) {
        final float fontScale = display.scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
