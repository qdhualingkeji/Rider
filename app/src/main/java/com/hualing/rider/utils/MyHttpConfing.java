package com.hualing.rider.utils;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MyHttpConfing {

    public static final String tag = "TAG-->";

    public static final String baseUrl = "http://120.27.5.36:8080/htkApp/API/riderAPI/";//客户版本外网

    /* 获得待抢单数据 */
    public static final String getDaiQiangDan = baseUrl + "getDaiQiangDan";
    /* 用户登录 */
    public static final String login = baseUrl + "login";
    /* 用户注册 */
    public static final String registered = baseUrl + "register";
    /* 确认抢单 */
    public static final String confirmQiangDan = baseUrl + "confirmQiangDan";
    /* 确认取货 */
    public static final String confirmQuHuo = baseUrl + "order/itemsToShip";
    /* 获得待取货数据 */
    public static final String getDaiQuHuo = baseUrl + "getDaiQuHuo";
    /* 确认送达 */
    public static final String confirmSongDa = baseUrl + "enterReceipt";
    /* 获得待抢单详情数据 */
    public static final String getDaiQiangDanDetail = baseUrl + "getDaiQiangDanDetail";
    /* 获得待送达数据 */
    public static final String getDaiSongDa = baseUrl + "getDaiSongDa";

}



