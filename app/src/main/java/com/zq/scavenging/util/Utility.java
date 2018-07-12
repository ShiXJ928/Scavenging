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
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String getEquipName(String str) {
        String name = "";
        switch (str.substring(2, 4)) {
            case "01":
                name = "警用装具箱";
                break;
            case "02":
                name = "警用装备包";
                break;
            case "03":
                name = "橡胶警棍";
                break;
            case "04":
                name = "手持喊话器";
                break;
            case "05":
                name = "防弹头盔";
                break;
            case "06":
                name = "探照灯";
                break;
            case "07":
                name = "防暴盾牌";
                break;
            case "08":
                name = "防暴盔甲服";
                break;
            case "09":
                name = "远射灯";
                break;
            case "0A":
                name = "破门工具";
                break;
            case "0B":
                name = "警用催泪喷射器";
                break;
            case "0C":
                name = "防化服";
                break;
            case "0D":
                name = "灭火毯";
                break;
            case "0E":
                name = "防暴头盔";
                break;
            case "0F":
                name = "防弹防刺服";
                break;
            case "10":
                name = "破胎器";
                break;
            case "11":
                name = "牵引带";
                break;
            case "12":
                name = "警用防暴射网器";
                break;
            case "13":
                name = "痕迹现场勘查包";
                break;
//            case "14":
//                name="打捞器";
//                break;
            case "15":
                name = "打捞器";
                break;
            case "16":
                name = "反光背心";
                break;
            case "17":
                name = "反光锥筒";
                break;
            case "18":
                name = "防刺服";
                break;
            case "19":
                name = "长警棍";
                break;
            case "1A":
                name = "防弹衣";
                break;
            case "1B":
                name = "发光指挥棒";
                break;
//            case "1C":
//                name="痕迹现场勘查包";
//                break;
            case "1D":
                name = "痕迹现场勘查包";
                break;
            case "1E":
                name = "模拟350兆对讲机";
                break;
            case "1F":
                name = "防弹盾牌";
                break;
            case "20":
                name = "警戒带";
                break;
            case "21":
                name = "电子体温计";
                break;
            case "22":
                name = "二代身份证阅读器";
                break;
            case "23":
                name = "卫星电话机";
                break;
            case "24":
                name = "约束带";
                break;
            case "25":
                name = "搜索灯";
                break;
            case "26":
                name = "警笛";
                break;
            case "27":
                name = "望远镜";
                break;
            case "28":
                name = "防弹盾牌";
                break;
//            case "29":
//                name="软质防弹头盔";
//                break;
            case "2A":
                name = "软质防弹头盔";
                break;
            case "2B":
                name = "皮尺";
                break;
//            case "2C":
//                name = "防弹防刺服";
//                break;
            case "2D":
                name = "防弹防刺服";
                break;
            case "2E":
                name = "停车示意牌";
                break;
            case "2F":
                name = "无线对讲机";
                break;
            case "30":
                name = "救生衣";
                break;
            case "31":
                name = "防爆毯";
                break;
            case "32":
                name = "发电机";
                break;
            case "33":
                name = "防毒衣";
                break;
            case "34":
                name = "手机信号屏蔽器";
                break;
//            case "35":
//                name = "其他防护装备";
//                break;
//            case "36":
//                name = "警笛";
//                break;
            case "37":
                name = "其他防护装备";
                break;
            case "38":
                name = "防弹衣";
                break;
//            case "39":
//                name="软质防弹头盔";
//                break;
            case "3A":
                name = "普通脚镣";
                break;
            case "3B":
                name = "　约束带";
                break;
            case "3C":
                name = "警用水壶";
                break;
            case "3D":
                name = "多功能破拆工具";
                break;
            case "3E":
                name = "手持金属探测器";
                break;
            case "3F":
                name = "监控系统";
                break;
            case "40":
                name = "伸缩路锥";
                break;
            case "41":
                name = "强光手电";
                break;
            case "42":
                name = "警用制式刀具";
                break;
            default:
                break;
        }
        return name;
    }

    public static String getShelvesName(String str) {
        String name = "";
        switch (str.substring(4, 6)) {
            case "01":
                name = "货架A";
                break;
            case "02":
                name = "货架B";
                break;
            case "03":
                name = "货架C";
                break;
            case "04":
                name = "货架D";
                break;
            case "05":
                name = "货架E";
                break;
            case "06":
                name = "货架F";
                break;
            case "07":
                name = "货架G";
                break;
            case "08":
                name = "货架H";
                break;
            case "09":
                name = "货架I";
                break;
            case "0A":
                name = "货架J";
                break;
            case "0B":
                name = "货架K";
                break;
            case "0C":
                name = "货架L";
                break;
            case "0D":
                name = "货架M";
                break;
            case "0E":
                name = "货架N";
                break;
            case "0F":
                name = "货架O";
                break;
            case "10":
                name = "货架P";
                break;
            case "11":
                name = "货架Q";
                break;
            case "12":
                name = "货架R";
                break;
            case "13":
                name = "货架S";
                break;
            case "14":
                name = "货架T";
                break;
            case "15":
                name = "货架U";
                break;
            case "16":
                name = "货架V";
                break;
            case "17":
                name = "货架W";
                break;
            case "18":
                name = "货架X";
                break;
            case "19":
                name = "货架Y";
                break;
            case "1A":
                name = "货架Z";
                break;
            default:
                name = "货架" + str.substring(4, 6);
                break;
        }
        switch (str.substring(6, 8)) {
            case "01":
                name = name + "-1层";
                break;
            case "02":
                name = name + "-2层";
                break;
            case "03":
                name = name + "-3层";
                break;
            case "04":
                name = name + "-4层";
                break;
            case "05":
                name = name + "-5层";
                break;
            case "06":
                name = name + "-6层";
                break;
            default:
                name = name + "-" + str.substring(6, 8) + "层";
                break;
        }
        return name;
    }

    public static String getNowTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getTimeStr(String str, String oldtype, String newType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(oldtype);
            Date date = sdf.parse(str);
            sdf = new SimpleDateFormat(newType);
            str = sdf.format(date);
        } catch (Exception e) {

        }
        return str;
    }
}
