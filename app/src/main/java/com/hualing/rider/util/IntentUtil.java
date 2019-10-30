package com.hualing.rider.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hualing.rider.R;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class IntentUtil {

    public static void openActivity(Activity context, Class dClass){
        Intent intent = new Intent(context,dClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    public static void openActivityForResult(Activity context, Class dClass, int requestCode, Bundle bundle){
        Intent intent = new Intent(context,dClass);
        if (bundle==null) {
            context.startActivityForResult(intent,requestCode);
        }else{
            intent.putExtras(bundle);
            context.startActivityForResult(intent,requestCode);
        }
        context.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum
     */
    public static void callPhone(String phoneNum,Activity context){
        //Intent intent = new Intent(Intent.ACTION_CALL);//直接拨打电话，需要在AndroidMenifest文件里加上这个权限：<uses-permission android:name="android.permission.CALL_PHONE" />，在Android6.0中，还要在代码中动态申请权限
        Intent intent = new Intent(Intent.ACTION_DIAL);//跳转到拨号界面，用户手动点击拨打，不需要申请权限，可以直接跳转到拨号界面
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

}
