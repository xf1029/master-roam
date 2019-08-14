package com.a51tgt.t6.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.PackageInfo;

/**
 * Edited by Chen Jin on 2019/07/18.
 * Created by liu_w on 2017/12/17.
 */

public class PackageInfoView extends LinearLayout implements View.OnClickListener {
    private TextView tv_package_name, tv_flow_count, tv_flow_left, tv_flow_countries,
            tv_package_starttime, tv_package_endtime,
            tv_in_using;
    PackageInfo myPackageInfo;

    public PackageInfoView(final Context context, final PackageInfo packageInfo){
        super(context);
        Log.i("contenxt",context.toString());
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_package_info, this);
        tv_package_name = convertView.findViewById(R.id.tv_package_name);
        tv_flow_count = convertView.findViewById(R.id.tv_flow_count);
        tv_flow_left = convertView.findViewById(R.id.tv_flow_left);
        tv_flow_countries = convertView.findViewById(R.id.tv_flow_countries);
        tv_package_starttime = convertView.findViewById(R.id.tv_package_starttime);
        tv_package_endtime = convertView.findViewById(R.id.tv_package_endtime);
        tv_in_using = convertView.findViewById(R.id.tv_in_using);

        if(packageInfo != null){
            tv_package_name.setText(packageInfo.product_name);
            Log.i("contenxt",packageInfo.product_name);

            if(packageInfo.flow_count.equals("unlimited")){
                tv_flow_count.setText(getResources().getString(R.string.tag_unlimited));
            } else
                tv_flow_count.setText(packageInfo.flow_count);

            if(packageInfo.left_flow_count.equals("unlimited")){
                tv_flow_left.setText(getResources().getString(R.string.tag_unlimited));
            } else
                tv_flow_left.setText(packageInfo.left_flow_count);

            if(packageInfo.countries.length() > 40)
                tv_flow_countries.setText(packageInfo.countries.substring(0, 40) + "...");
            else
                tv_flow_countries.setText(packageInfo.countries);

            tv_package_starttime.setText(packageInfo.start_date);
            tv_package_endtime.setText(packageInfo.end_date);

            if(packageInfo.status.equals("INUSE")){
                tv_in_using.setVisibility(VISIBLE);
            }
        }

        tv_flow_countries.setOnClickListener(this);
        myPackageInfo = packageInfo;
    }

    public PackageInfoView(Context context){
        super(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_package_info, this);
        tv_package_name = convertView.findViewById(R.id.tv_package_name);
        tv_flow_count = convertView.findViewById(R.id.tv_flow_count);
        tv_flow_left = convertView.findViewById(R.id.tv_flow_left);
        tv_flow_countries = convertView.findViewById(R.id.tv_flow_countries);
        tv_package_starttime = convertView.findViewById(R.id.tv_package_starttime);
        tv_package_endtime = convertView.findViewById(R.id.tv_package_endtime);
        tv_in_using = convertView.findViewById(R.id.tv_in_using);
    }

    public void setData(final PackageInfo packageInfo){

        if(packageInfo != null){
            tv_package_name.setText(packageInfo.product_name);

            tv_flow_count.setText(packageInfo.flow_count);
            tv_flow_left.setText(packageInfo.left_flow_count);
            if(packageInfo.countries.length() > 40)
                tv_flow_countries.setText(packageInfo.countries.substring(0, 40) + "...");
            else
                tv_flow_countries.setText(packageInfo.countries);
            tv_package_starttime.setText(packageInfo.start_date);
            tv_package_endtime.setText(packageInfo.end_date);
            tv_flow_countries.setOnClickListener(this);

            myPackageInfo = packageInfo;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_flow_countries:
                if(myPackageInfo != null){
                    if(tv_flow_countries.getText().length() <= 45)
                        tv_flow_countries.setText(myPackageInfo.countries);
                    else
                        tv_flow_countries.setText(myPackageInfo.countries.substring(0, 40) + "...");
                }
                break;
        }
    }
}
