package com.hualing.rider.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hualing.rider.global.TheApplication;


/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class SharedPreferenceUtil {

    /**
     * 记住是什么身份使用程序
     */
    public static void setUserType(int userType){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userType",userType);
        editor.commit();
    }

    /**
     * 记住用户名、密码
     * @param phone
     * @param password
     */
    public static void rememberRider(String phone, String password){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone",phone);
        editor.putString("password",password);
        editor.commit();
    }

    public static boolean ifHasLocalRiderInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        if (TextUtils.isEmpty(preferences.getString("phone",null))||TextUtils.isEmpty(preferences.getString("password",null))) {
            return false;
        }
        return true;
    }

    /**
     * 获取上次登陆的用户信息
     * @return
     */
    public static String[] getRiderInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        String userName = preferences.getString("phone",null);
        String password = preferences.getString("password",null);
        return new String[]{userName,password};
    }

    /**
     * 注销登录
     */
    public static void logout(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone","");
        editor.putString("password","");
        editor.commit();
    }

    /**
     * 获取加载页地址
     */
    public static String getLoadPageUrl(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("loadPageUrl",null);
    }

    /**
     * 获取加载页地址
     */
    public static void setLoadPageUrl(String url){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loadPageUrl", url);
        editor.commit();
    }

    /**
     * 保存是否有未读消息
     */
    public static void saveIfHasUnreadMsg(boolean flag){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ifHasUnreadMsg", flag);
        editor.commit();
    }

    /**
     * 查看是否有未读消息
     */
    public static boolean checkIfHasUnreadMsg() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getBoolean("ifHasUnreadMsg",false);
    }
}
