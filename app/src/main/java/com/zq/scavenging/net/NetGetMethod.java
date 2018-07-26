package com.zq.scavenging.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zq.scavenging.acty.BaseActy;
import com.zq.scavenging.bean.Result;
import com.zq.scavenging.util.ToastUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义get网络读取框架
 * Created by zy on 2016/4/21.
 */
public abstract class NetGetMethod {
    private static BaseActy ac;

    public NetGetMethod(final BaseActy ac, final String url, ThreadPoolExecutor cachedThreadPool, Object... args) {
        if (ac != null) {
            this.ac = ac;
        }
        final URL uri;
        try {
            uri = new URL(String.format((url), args));
            Log.e("get:uri", uri.toString());
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Result result = JSON.parseObject(Net.httpGetMethod(uri).toString(), Result.class);
                        if (!ac.isFinishing()) {
                            if (result.getCode() == 1) {  //存在-1警告提示
                                runSuccsess(result);
                            } else {
                                runfail(ac, result.getValue().toString());
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
