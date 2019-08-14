package com.a51tgt.t6.comm;


import android.os.Environment;

import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.DevicePackageInfo;

import java.net.Socket;

/**
 * Created by liuke on 11/10/2016.
 */

public final class APIConstants {

//    public final static String mac_address = BuildConfig.DEBUG ? "40:45:DA:96:3C:B8" : "";
//    public final static String server_domain = BuildConfig.DEBUG ? /*"192.168.0.168/TGT" : "as2.51tgt.com";//*/"mall.51tgt.com":"mall.51tgt.com";
//    public final static String server_domain = BuildConfig.DEBUG ? "as2.51tgt.com":"mall.51tgt.com";
    /**********   更改环境   ************/
//    private final static String server_domain = "apollo.51tgt.com:8060/api-publicappmodule";
   public final static String server_domain = "47.103.163.250/api-publicappmodule";
    /**********   更改Stripe秘钥   ************/
    //测试秘钥
//    public final static String Stripe_key = "pk_test_wmOvlNMAN1YFJO3hqF7HA0Mt00nIWOBsEx";
    //生产秘钥
    public final static String Stripe_key = "pk_live_DozdZoItDsAMPZ2nZa9pbtQM00FXAkFPY7";

    public final static String server_host = "http://" + server_domain;

    //1. 判断设备是否激活
    public final static String Get_Device_Status = server_host + "/tgtapp/checkdevice";
    //2. 激活设备
    public final static String Device_Active = server_host + "/tgtapp/activedevice";
    //3. 获取设备绑定的订单信息
    public final static String Get_Flow_Package = server_host + "/tgtapp/georderinfobycode";
    //4. 根据app标识获取提供的产品类型 （暂不调试）
    public final static String Get_Flow_MallGroup = server_host + "/tgtapp/getproductareas";


    //5. 根据产品区域获取产品
        //原来用来获取商品产品列表
    public final static String Get_Flow_Mall = server_host + "/tgtapp/getproductbyarea";
    //6.订单提交与支付
        //a. 支付宝/微信
    public final static String Payment= server_host + "/tgtapp/submitorder";
        //b. 信用卡
    public final static String Payment_Card = server_host + "/tgtapp/submitorderbycard";
    //7.查询支付结果
    public final static String Query = server_host + "/tgtapp/queryPaymentResult";

    //一些常数
    public final static String Account_ID = "tgt_salesapp";
    public static String sn = "";
    public static Socket socket=null ;
    public static String alertInfo="" ;

    public static String currentLan = "";
    public final static String WX_APP_ID = "wxda1e548f5020ba9e";



    //以下是未更改的
    public final static String Get_FYJApp_Info = server_host + "/FyjApp/GetFYJAppInfo";
    public final static String Get_Download_App = server_host + "/download";

//    public final static String Purchase_Product = server_host + "/FyjApp/WxPurchaseProduct";
//    public final static String Purchase_Product_alipay = server_host + "/FyjApp/ALPayProduct";
//    public final static String Purchase_Product_StripePay = server_host + "/FyjApp/StripePayProduct";

//    public final static String Query_WxOrder = server_host + "/FyjApp/QueryWxOrder";
//    public final static String Query_alipayOrder = server_host + "/FyjApp/QueryALPayOrder";
//    public final static String Purchase_Product_credit = server_host + "/FyjApp/APPayProduct";
    public final static String Query_CreditOrder = server_host + "/FyjApp/QueryAPPayOrder";
//    public final static String Get_Instruc = server_host + "/download/instructions/instructions.json";

    public static DeviceInfo deviceInfo = null;
    public  static  Boolean isReStartService= false;
    public static String deviceVersion = null;
    public static DevicePackageInfo devicePackageInfo = null;
    public static boolean isBluetoothConnection = false;
    public static String newPassword = null;
    public static int newMode = -1;

    public static boolean isScanNewDevice = false;

    private final static String FILE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tuge/";
    public final static String FILE_DOWNLOAD = FILE_ROOT + "Download/";

    public static String OUT_TRADE_NO = "";

    public final static int SCANNING_REQUEST_CODE = 0x1001;

    public static final String BR_ORDER_STATUS = "BR_ORDER_STATUS";
    public static final String BR_LAN_STATUS = "BR_LAN_STATUS";

    // 简体中文
    public static final String SIMPLIFIED_CHINESE = "zh";
    // 英文
    public static final String ENGLISH = "en";
    // 繁体中文


}
