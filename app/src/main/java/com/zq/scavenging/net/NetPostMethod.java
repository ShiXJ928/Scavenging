package com.zq.scavenging.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zq.scavenging.acty.BaseActy;
import com.zq.scavenging.bean.Result;
import com.zq.scavenging.util.ToastUtil;

import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 自定义get网络读取框架
 * Created by zy on 2016/4/21.
 */
public abstract class NetPostMethod {
    private static BaseActy ac;

    public NetPostMethod(final BaseActy ac, final String url, ThreadPoolExecutor cachedThreadPool, final Object post, Object... args) {
        if (ac != null) {
            this.ac = ac;
        }
        final URL uri;
        try {
            Log.e("post:string", post.toString());
            uri = new URL(String.format((url), args));
            Log.e("post:uri", uri.toString());
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Result result = JSON.parseObject(Net.httpPostMethod(uri, post).toString(), Result.class);
                        if (!ac.isFinishing()) {
                            if (result.getError_code() == 0) {  //存在-1警告提示
                                runSuccsess(result);
                            } else {
                                runfail(ac, result.getMessage());
                            }
                        }
                    } catch (NullPointerException e) {
                        if (!ac.isFinishing()) {
                            serverfail();
                        }

                    } finally {
                        if (!ac.isFinishing()) {
                            netfinal();
                        }
                    }
                }
            });
        } catch (Exception e) {
            ac.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    new WarningDialog(ac, "连接服务器失败", 0).show();
                    ToastUtil.show("您的网络不太给力，请稍后再试");
                }
            });
        }
    }


    public abstract void runSuccsess(Result r); //成功的时候

    public void runfail(Context ctx, String message) {

    }

    public abstract void serverfail(); //服务器连接失败

    public void netfinal() {

    }  //网络请求完成

    public static void showServerWarinning() {  //服务器连接失败
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show("服务器连接失败");
            }
        });
    }

    public static void showFailWarinning(final Context ctx, final String message) {  //失败提示
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(message);
            }
        });
    }
}
