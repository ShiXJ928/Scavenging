package com.zq.scavenging.util;

import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/10/18.
 */

public class SharedUtility {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public void loginOut() {
        editor.commit();
    }

    public String getToken() {
        return pref.getString("token", "");
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getName() {
        return pref.getString("name", "");
    }

    public void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String getTime() {
        return pref.getString("time", "");
    }

    public void setTime(String time) {
        editor.putString("time", time);
        editor.commit();
    }
}
