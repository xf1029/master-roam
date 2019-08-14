package com.a51tgt.t6.bean;

import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.utils.TipUtil;
import com.google.gson.Gson;

import java.util.LinkedHashMap;

/**
 * Created by liu_w on 2017/10/22.
 */

public class HttpResponseData {
    public int status;
    public int code;
    public String msg;

    public LinkedHashMap<String, Object> data;
    //public JSONObject data;

    public HttpResponseData(String json){
        try{
            Gson gson = new Gson();
            HttpResponseData obj = gson.fromJson(json, HttpResponseData.class);
            if(obj != null) {
                status = obj.status;
                code = obj.code;
                msg = obj.msg;
                data = obj.data;
            }
        }
        catch (Exception ex){
        }
    }
}
