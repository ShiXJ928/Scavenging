package com.zq.scavenging.util;

import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/10/18.
 */

public class SharedUtility {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public void loginOut() {
        editor.remove("token");
        editor.remove("pwd");
        editor.remove("name");
        editor.commit();
    }

    public String getToken() {
        return pref.getString("token", "");
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getAccount() {
        return pref.getString("account", "");
    }

    public void setAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public String getPwd() {
        return pref.getString("pwd", "");
    }

    public void setPwd(String pwd) {
        editor.putString("pwd", pwd);
        editor.commit();
    }

    public String getName() {
        return pref.getString("name", "");
    }

    public void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }
}
