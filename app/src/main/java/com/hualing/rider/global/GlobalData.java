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
    // 用户Id
    public static String userId;

    //昵称
    public static String userName ;

    //真实姓名
    public static String realName ;

    //审核权限组，有19的话就是领导（负责人），反之是质检员
    public static String checkQXGroup ;

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
    //关于我们
    public static String company;
    public static String phone;

    public static String upDate = null;

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

    private static String loginName;
    private static String password;
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
        /********************
         * 通用
         ***********************/
        /* 服务模板 */
        public static final String TEMPLATE = "999";

        /* 初始化服务 */
        public static final String INIT = "000";
        /* 用户登录 */
        public static final String LOGIN = "login";
        /* 获得待抢单数据 */
        public static final String GET_DAI_QIANG_DAN = "getDaiQiangDan";
        /* 获得待抢单详情数据 */
        public static final String GET_DAI_QIANG_DAN_DETAIL = "getDaiQiangDanDetail";
        /* 获得待取货数据 */
        public static final String GET_DAI_QU_HUO = "getDaiQuHuo";

        /*获取需要录入的数据*/
        public static final String GET_INPUTED_DATA = "getInputedData";

        /*提交需要录入的数据*/
        public static final String COMMIT_INPUTED_DATA = "commitInputedData";

        /*获取主页数据（主要是可用功能权限）*/
        public static final String GET_MAIN_DATA = "getMainData";


        /* 来料入库/余料退库 */
        public static final String MATERAIL_IN = "100";
        /* 原料出库/车间领料 */
        public static final String MATERAIL_OUT= "101";
        /* 半成品/成品入库 */
        public static final String PRODUCT_IN = "102";
        /* 成品出库/产品入库 */
        public static final String PRODUCT_OUT = "103";

        public static final String COMMIT_MATERIALIN_INPUTED_DATA = "createWLIn_M";
        public static final String CREATE_WL_RKD = "createWL_RKD";
        public static final String CREATE_WL_CKD = "createWL_CKD";
        public static final String GET_PDT_SORT = "getPdtSort";
        public static final String GET_HL_SORT = "getHlSort";
        public static final String GET_HL_PRODUCT = "getHlProduct";
        public static final String GET_WL_OUT_SHOW_DATA = "getWlOutShowData";
        public static final String WL_OUT = "wlOut";
        public static final String GET_DEPARTMENT_DATA = "getDepartmentData";
        public static final String CREATE_WL_TKD = "createWL_TKD";
        public static final String GET_WL_TK_SHOW_DATA = "getWlTKShowData";
        public static final String WL_TK = "wlTk";
        public static final String GET_WL_THROW_SHOW_DATA = "getWlTLShowData";
        public static final String WL_THROW = "wlThrow";
        public static final String GET_GX = "getGX";
        public static final String GET_CJ = "getCJ";
        public static final String CREATE_BCP_RKD = "createBcpRkd";
        public static final String BCP_IN = "bcpIn";
        public static final String GET_TLYL = "getTLYL";  //获取两个投料表里的数据
        public static final String GET_BCP_THROW_SHOW_DATA = "getBcpTLShowData";
        public static final String BCP_THROW = "bcpThrow";
        public static final String CREATE_BCP_TKD = "createBCP_TKD";
        public static final String GET_BCP_TK_SHOW_DATA = "getBcpTkShowData";
        public static final String BCP_TK = "bcpTK";
        public static final String CREATE_BCP_CKD = "createBCP_CKD";
        public static final String GET_LEI_BIE = "getLeiBie";
        public static final String BIG_CP_IN = "bigCpIn";
        public static final String SMALL_CP_IN = "smallCpIn";
        public static final String GET_BIG_CP_DATA = "getBigCpData";
        public static final String BIG_CP_OUT = "bigCpOut";
        public static final String GET_BIG_CP_OUT_DATA = "getBigCpOutData";
        public static final String GET_SMALL_CP_OUT_DATA = "getSmallCpOutData";
        public static final String SMALL_CP_OUT = "smallCpOut";
        public static final String WL_TRACK = "wlTrack";
        public static final String BCP_TRACK = "bcpTrack";
        public static final String SMALL_CP_TRACK = "smallCpTrack";
        public static final String BIG_CP_TRACK = "bigCpTrack";
        public static final String SEND_NOTIFICATION = "sendNotification";
        public static final String GET_NON_CHECK_DATA = "getNonCheckData";
        public static final String GET_WLIN_VERIFY_DATA = "getWlInVerifyData";
        public static final String GET_WLIN_QUALITY_CHECK_DATA = "getWlInQualityCheckData";
        public static final String GET_WLOUT_VERIFY_DATA = "getWlOutVerifyData";
        public static final String GET_WLTK_VERIFY_DATA = "getWlTkVerifyData";
        public static final String GET_WLTK_QUALITY_CHECK_DATA = "getWlTkQualityCheckData";
        public static final String GET_BCPIN_VERIFY_DATA = "getBcpInVerifyData";
        public static final String GET_SMALL_CP_IN_VERIFY_DATA = "getSmallCPInVerifyData";
        public static final String GET_BCPIN_QUALITY_CHECK_DATA = "getBcpInQualityCheckData";
        public static final String GET_CPOUT_VERIFY_DATA = "getCpOutVerifyData";
        public static final String GET_BCPTK_VERIFY_DATA = "getBcpTkVerifyData";
        public static final String GET_BCPTK_QUALITY_CHECK_DATA = "getBcpTkQualityCheckData";
        public static final String AGREE_WLIN = "agreeWlIn";
        public static final String REFUSE_WLIN = "refuseWlIn";
        public static final String REFUSE_WLOUT = "refuseWlOut";
        public static final String AGREE_WLOUT = "agreeWlOut";
        public static final String AGREE_WLTK = "agreeWlTk";
        public static final String REFUSE_WLTK = "refuseWlTk";
        public static final String AGREE_BCP_IN = "agreeBcpIn";
        public static final String REFUSE_BCP_IN = "refuseBcpIn";
        public static final String REFUSE_BCP_OUT = "refuseBcpOut";
        public static final String AGREE_BCP_OUT = "agreeBcpOut";
        public static final String AGREE_BCP_TK = "agreeBcpTk";
        public static final String REFUSE_BCP_TK = "refuseBcpTk";
        public static final String GET_QUALITY_DATA = "getQualityData";
        public static final String PASS_CHECK = "passCheck";
        public static final String UPDATA_WLIN_DATA = "updateWlInData";
        public static final String UPDATA_WLOUT_DATA = "updateWlOutData";
        public static final String UPDATA_WLTK_DATA = "updateWlTkData";
        public static final String UPDATA_BCPIN_DATA = "updateBcpInData";
        public static final String UPDATA_CPOUT_DATA = "updateCpOutData";
        public static final String UPDATA_BCPTK_DATA = "updateBcpTkData";
        public static final String GET_PERSON_INFO = "getAllPerson";
        public static final String GET_PERSON_BY_ID = "getPersonById";
        public static final String SEARCH_ALL_PERSON = "searchAllPerson";
        public static final String GET_CAN_MODIFY_DATA = "getCanModifyData";
        public static final String GET_SMALLCP_IN_QUALITY_CHECKDATA = "getSmallCPInQualityCheckData";
        public static final String GET_XZQX = "getXZQX";
        public static final String REGISTERED = "register";
        public static final String UPDATE_USER_DATA = "updateUserData";
        public static final String DELETE_USER = "deleteUser";
    }
}
