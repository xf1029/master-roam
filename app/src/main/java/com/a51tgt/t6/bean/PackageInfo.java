package com.a51tgt.t6.bean;

import com.google.gson.internal.LinkedTreeMap;


/**
 * Edited by Chen Jin on 2019/07/18.
 */

public class PackageInfo {
    public String product_name;
    public String flow_count;
    public String left_flow_count;

    public String countries;
    public String start_date;
    public String end_date;

    public String status;


    public PackageInfo(LinkedTreeMap<String, Object> data){
        if(data != null){
            if(data.containsKey("productname"))
                product_name = data.get("productname").toString();

            if(data.containsKey("totalflow"))
                flow_count = data.get("totalflow").toString();

            if(data.containsKey("laveflow"))
                left_flow_count = data.get("laveflow").toString();

            if(data.containsKey("countryname"))
                countries = data.get("countryname").toString();

            if(data.containsKey("usestartdate"))
                start_date = data.get("usestartdate").toString();

            if(data.containsKey("useenddate"))
                end_date = data.get("useenddate").toString();

            if(data.containsKey("status"))
                status = data.get("status").toString();
        }
    }
}
