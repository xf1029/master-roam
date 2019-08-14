package com.a51tgt.t6.bean;

import java.util.Map;

/**
 * Created by liu_w on 2017/12/17.
 */

public class AreaInfo {
    public String group_name;

    public String title;
    public String img_url;



    public AreaInfo(Map<String, Object> data){
        if(data != null){
            if(data.containsKey("group_name"))
                group_name = data.get("group_name").toString();


            if(data.containsKey("title"))
                title = data.get("title").toString();

            if(data.containsKey("img_url"))
                img_url = data.get("img_url").toString();
        }
    }}
