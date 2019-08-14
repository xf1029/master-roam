package com.a51tgt.t6.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.PackageInfo;
import com.a51tgt.t6.comm.APIConstants;

/**
 * Created by liu_w on 2017/12/17.
 */

public class DeviceInfoView extends LinearLayout {
    private TextView tv_device_sn, tv_menu;
    private String sn, MacAddress;
    private Context mContext;

    public DeviceInfoView(final Context context, final String sn, final String mac_address){
        super(context);
        mContext = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_device_info, this);
        tv_device_sn = (TextView) convertView.findViewById(R.id.tv_device_sn);
        tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
        this.sn = sn;
        MacAddress = mac_address;
        tv_device_sn.setText(sn);

    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);
    }

    public DeviceInfoView(Context context){
        super(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.inflate(R.layout.view_package_info, this);
        tv_device_sn = (TextView) convertView.findViewById(R.id.tv_device_sn);
    }

    public String getSn(){
        return sn;
    }
    public String getMacAddress(){
        return MacAddress;
    }

    public void openMenu(){
        tv_menu.setText(R.string.tip_device_info_menu);
    }

    public void closeMenu(){
        tv_menu.setText(R.string.tip_connect_unbind);
    }
}
