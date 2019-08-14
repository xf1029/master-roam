package com.a51tgt.t6.bean;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;

public class DeviceOrderHttpResponseData {
    public int code;
    public String msg;
    public ArrayList<LinkedTreeMap<String, Object>> data;

    public DeviceOrderHttpResponseData(String json){
        try{
            Gson gson = new Gson();
            DeviceOrderHttpResponseData obj = gson.fromJson(json, DeviceOrderHttpResponseData.class);
            if(obj != null) {
                code = obj.code;
                msg = obj.msg;
                data = obj.data;
            }
        }
        catch (Exception ex){
        }
    }
}
