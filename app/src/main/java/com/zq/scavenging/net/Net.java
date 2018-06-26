package com.zq.scavenging.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zq.scavenging.application.App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by zy on 2016/3/11.
 * 网络相关方法
 */
public class Net {

    public static JSONObject httpPostMethod(URL uri, Object postData) {         //post方法获取json数据
        JSONObject recvData = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(20000);
            // 此方法在正式链接之前设置才有效。
            if (postData != null) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
            }
            if (App.sharedUtility.getToken() != null) {
                urlConnection.setRequestProperty("AppAuthorization", App.sharedUtility.getToken());
            }
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            if (postData != null) {
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(postData.toString());
                writer.close();
                outputStream.close();
            }
            // 开始GET数 获取代码返回值
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream is = null;
                is = urlConnection.getInputStream();
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append((line + "\n"));
                    }
                    recvData = JSON.parseObject(sb.toString());
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return recvData;
    }

    public static JSONObject httpGetMethod(URL uri) {
        JSONObject recvData = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(20000);
            urlConnection.setUseCaches(false);
            if (App.sharedUtility.getToken() != null) {
                urlConnection.setRequestProperty("AppAuthorization", App.sharedUtility.getToken());
                Log.e("---------->", App.sharedUtility.getToken());
            }
            // 正式创建链接
            urlConnection.connect();
            // 开始GET数 获取代码返回值
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream is = null;
                is = urlConnection.getInputStream();
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append((line + "\n"));
                    }
                    recvData = JSON.parseObject(sb.toString());
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return recvData;
    }
}
