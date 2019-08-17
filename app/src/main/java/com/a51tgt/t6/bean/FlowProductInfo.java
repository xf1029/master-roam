package com.a51tgt.t6.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Edited by Chen Jin on 2019/07/18
 * Created by liu_w on 2017/9/14.
 */

public class FlowProductInfo {
    public String coverImage = "";
    public String subTitle = "";
    public String productname = "";
    public String price = "", priceType = "";
    public String productid = "", productnumber = "", activedays = "", producttype = "";
    public String url = "";
    public String coverage = "", total_flow = "", effective_days = "", notice = "";


//    public FlowProductInfo(String info){
//        try {
//            JSONObject obj = new JSONObject(info);
//            coverImage = obj.getString("url");
//            title = obj.getString("productname");
//            subTitle = obj.getString("productname");
//            price = obj.getString("price");
//            priceType = obj.getString("currency");
//            moneyType = obj.getString("currency");
//            id = obj.getInt("id");
//            url = obj.getString("url");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public FlowProductInfo(Map<String, Object> data){
        if(data.containsKey("url") && data.get("url") != null)
            coverImage = data.get("url").toString();

        if(data.containsKey("productid") && data.get("productid") != null)
            productid = data.get("productid").toString();

        if(data.containsKey("productnumber") && data.get("productnumber") != null)
            productnumber = data.get("productnumber").toString();

        if(data.containsKey("activedays") && data.get("activedays") != null) {
            double d = Double.parseDouble(data.get("activedays").toString());
            int i = (int) d;
            activedays = String.valueOf(i);
        }

        if(data.containsKey("producttype") && data.get("producttype") != null)
            producttype = data.get("producttype").toString();

        if(data.containsKey("price") && data.get("price") != null) {
            double d = Double.parseDouble(data.get("price").toString());
//            int i = (int) d;
            price = String.valueOf(d);
        }

        if(data.containsKey("currency") && data.get("currency") != null)
            priceType = data.get("currency").toString();

        if(data.containsKey("url") && data.get("url") != null)
            url = data.get("url").toString();

        if(data.containsKey("productname") && data.get("productname") != null)
            productname = data.get("productname").toString();

        if(data.containsKey("sub_title")&&data.get("sub_title")!=null)
            subTitle = data.get("sub_title").toString();

        if(data.containsKey("countryset") && data.get("countryset") != null)
            coverage = data.get("countryset").toString();

        if(data.containsKey("totalflow") && data.get("totalflow") != null)
            total_flow = data.get("totalflow").toString();

        if(data.containsKey("usedays") && data.get("usedays") != null)
        {
            double d = Double.parseDouble(data.get("usedays").toString());
            int i = (int) d;
            effective_days = String.valueOf(i);
        }

        if(data.containsKey("notice") && data.get("notice") != null)
            notice = data.get("notice").toString();
    }
}

