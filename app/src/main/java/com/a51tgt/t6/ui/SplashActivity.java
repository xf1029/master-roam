package com.a51tgt.t6.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import com.a51tgt.t6.MZApplication;
import com.a51tgt.t6.R;
import com.a51tgt.t6.abstract_face.OnNoticeUI;
import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.DevicePackageInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.bean.UserDataUtils;
import com.a51tgt.t6.bluetooth.BluetoothUtil;
import com.a51tgt.t6.bluetooth.ClsUtils;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.comm.TcpConfig;
import com.a51tgt.t6.lib.Capture2Activity;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.net.TcpUtil;
import com.a51tgt.t6.utils.AppLanguageUtils;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.DeviceInfoUtils;
import com.a51tgt.t6.utils.NetWorkUtils;
import com.a51tgt.t6.utils.TipUtil;
import com.a51tgt.t6.utils.WiFiUtil;

import java.util.Locale;

public class SplashActivity extends BaseActivity {

    private static final long DELAY_1S = 2000;
    private Context mContext;
    private View anchor;
    private WifiManager wifiManager;
    private String deviceMacAddress = "";
    private String deviceSSID = "";
    Handler waitWifiEnableHandler = new Handler();
    Runnable waitWifiEnableRunnable = new Runnable(){
        @Override
        public void run() {
//            SelectWifi();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        anchor = (View) findViewById(R.id.anchor);
        mContext = this;
//        EnableWifiReceiver receiver = new EnableWifiReceiver();

        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String name = preferences.getString("lang","");//getSt
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (name.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE; // 设置当前语言配置为简体
            getResources().updateConfiguration(config, dm); // 更新配置文件
        } else if(name.equals("en")){
            config.locale = Locale.ENGLISH; // 设置当前语言配置为英语
            getResources().updateConfiguration(config, dm); // 更新配置文件
        }
        else {
            config.locale = Locale.ENGLISH; // 设置当前语言配置为英语
            getResources().updateConfiguration(config, dm); // 更新配置文件
        }

        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);

//            IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
//            registerReceiver(receiver, filter);

        }
        else{
//            IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//            registerReceiver(receiver, filter);
//            getDeviceInfoHandler.postDelayed(getDeviceInfoRunnable, DELAY_1S);
        }
//        Integer time = 2000;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //40:45:DA:96:3C:B8
        //40:45:DA:A6:B2:56
//        deviceMacAddress = UserDataUtils.getInstance(mContext).getMacAddress();
//        deviceSSID = UserDataUtils.getInstance(mContext).getSsid();

//        if (!BluetoothUtil.getInstance().isBluetoothEnable()) {
//            BluetoothUtil.getInstance().enableBluetooth(this);//发起打开蓝牙
//        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (TextUtils.isEmpty(deviceMacAddress) || TextUtils.isEmpty(deviceSSID)) {
//                    Intent intent = new Intent(mContext, FirstScanActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    APIConstants.deviceInfo = new DeviceInfo(deviceSSID);
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                Intent intent = new Intent(mContext, FirstScanActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_1S);
}

        @Override
    protected void onResume() {
        super.onResume();

//        MZApplication.getInstance().setOnNoticeUiListener(this, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MZApplication.getInstance().setOnNoticeUiListener(null, OnNoticeUI.KEY_TYPE_SPLASH_ACTIVITY);
    }
}
