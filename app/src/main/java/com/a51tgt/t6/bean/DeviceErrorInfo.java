package com.a51tgt.t6.bean;

import com.a51tgt.t6.utils.CommUtil;

/**
 * Created by liu_w on 2017/9/18.
 */

public class DeviceErrorInfo {
    public String SSID;
    public String startTime;
    public String errorInfo;

    public DeviceErrorInfo(String ssid, String error_info){
        SSID = ssid;
        startTime = CommUtil.getCurTime();
        errorInfo = error_info;
    }

}
