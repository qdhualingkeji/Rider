package com.hualing.rider.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hualing.rider.BuildConfig;
import com.hualing.rider.aframework.yoni.YoniClient;
import com.hualing.rider.model.CPType;
import com.hualing.rider.model.FunctionType;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class GlobalData {
    public static Integer riderID;

    public static String phone ;

    public static String password ;

    public static String trueName ;

    //当前功能业务线
    public static int currentFunctionType = FunctionType.NON_SELECTED ;

    //当前功能业务线
    public static int currentCPInType = CPType.NON_SELECTED ;

    // 业务服务器地址
//    public static String mainServiceUrl;
    // 资源服务器地址
//    public static String resourceServiceUrl;

    public static String packageName;
    public static int verCode;
    public static String verName;
    public static String appId;
    public static String appName;

    //启动页的图片id
    private static int launchDrawableId ;
    //启动页的停留时间
    private static long launchDelayDuration ;


    public static long getLaunchDelayDuration() {
        return launchDelayDuration;
    }

    public static void setLaunchDelayDuration(long launchDelayDuration) {
        GlobalData.launchDelayDuration = launchDelayDuration;
    }

    public static int getLaunchDrawableId() {
        return launchDrawableId;
    }

    public static void setLaunchDrawableId(int launchDrawableId) {
        GlobalData.launchDrawableId = launchDrawableId;
    }

    public static void Load(Context context) {

//        if (BuildConfig.API_DEBUG) {
//            mainServiceUrl = context.getResources().getString(R.string.main_service_url_debug);
//            resourceServiceUrl = context.getResources().getString(R.string.main_resource_url_debug);
//        } else {
//            mainServiceUrl = context.getResources().getString(R.string.main_service_url);
//            resourceServiceUrl = context.getResources().getString(R.string.main_resource_url);
//        }
        YoniClient.getInstance().setBaseUrl(BuildConfig.SERVICE_URL);
        YoniClient.getInstance().setResourceUrl(BuildConfig.RESOURCE_URL);
        YoniClient.getInstance().setDebug(BuildConfig.API_DEBUG);

//        YoniClient.getInstance().create(MainDao.class);
//        YoniClient.getInstance().create(BusinessDao.class);
//        YoniClient.getInstance().create(UserDao.class);
        //YoniClient.getInstance().addInterceptor(new AuthorizationInterceptor());
//        //在登陆成功后设置
//        GlobalData.userId = "xxxx";
//        YoniClient.getInstance().setUser(GlobalData.userId);
//        //退出登陆之后设置
//        GlobalData.userId = "";
//        YoniClient.getInstance().setUser(GlobalData.userId);



        DeviceInfo.load(context);//获取手机信息
        AppInfo.load(context);//获取app信息

    }

    private static boolean isFirstOpen = true;

    public static boolean getIfFirstOpen(){
        return TheApplication.getSharedPreferences().getBoolean("isFirstOpen",true);
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static class AppInfo {
        static void load(Context context) {
            PackageManager manager;

            PackageInfo info = null;

            manager = context.getPackageManager();

            try {

                info = manager.getPackageInfo(context.getPackageName(), 0);

            } catch (PackageManager.NameNotFoundException e) {

                e.printStackTrace();

            }
            verCode = info.versionCode;//版本号
            verName = info.versionName;//版本名
            packageName = info.packageName;//包名
            appId = BuildConfig.APP_ID;// ios 还是android（101100）
            appName = BuildConfig.APP_NAME;//app名称
        }
    }

    public static String osVersion;
    public static String deviceModel;
    public static int osType = 1;
    public static String deviceId = "";

    public static class DeviceInfo {
        static void load(Context context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);//获取手机服务信息
            //            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            //            rxPermissions
            //                    .request(Manifest.permission.READ_PHONE_STATE
            //                    )
            //                    .subscribe(granted -> {
            //                        if (granted) {
            //                            deviceId = tm.getDeviceId();//获取手机设备号
            //                        } else {
            //                            // 有一个不允许后执行
            //                        }
            //                    });

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Build.SERIAL;
            }
            osVersion = Build.VERSION.SDK.replace("Android ", "");//获取手机api版本号
            deviceModel = Build.MODEL;//手机版本型号
        }
    }

    /**
     * 各种DEBUG参数
     */
    public static class Debug {
        /**
         * 全局DEBUG
         */
        public static boolean global = false;
    }


    public static class Service {

    }
}
