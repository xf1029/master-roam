package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.FlowMallAdapter;
import com.a51tgt.t6.bean.DeviceInfo;
import com.a51tgt.t6.bean.DeviceOrderHttpResponseData;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.TipUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu_w on 2017/9/13.
 */

public class FlowMallFragment extends Fragment {

    private RecyclerView rv_flow_package;
    private List<FlowProductInfo> flowProductInfoList;
    private FlowMallAdapter flowMallAdapter;
    private Dialog mWeiboDialog;

    public FlowMallFragment(){
    }
    public static FlowMallFragment newInstance(){
        FlowMallFragment fragment = new FlowMallFragment();
        return fragment;
    }
    //接收广播的处理
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
//            FlowMallFragment.this.recreate();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册接受的广播
        IntentFilter filter = new IntentFilter(APIConstants.BR_LAN_STATUS);
//        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flow_mall, container, false);
        rv_flow_package = rootView.findViewById(R.id.rv_flow_package);
        rv_flow_package.setHasFixedSize(true);
        rv_flow_package.setLayoutManager(new LinearLayoutManager(getActivity()));

        String lan,currency;

        if (APIConstants.currentLan.contains("zh")){
            lan = "zh-CN";
            currency = "RMB";
        }else{
            lan = "en";
            currency = "USD";
        }

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
        params[0] = new OkHttpClientManager.Param("device_no",  APIConstants.sn);
        params[1] = new OkHttpClientManager.Param("currency",  currency);

        params[2] = new OkHttpClientManager.Param("lang", lan);

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), getResources().getString(R.string.loading));

        new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume

            if(APIConstants.isScanNewDevice == true){
                String lan,currency;

                if (APIConstants.currentLan.contains("zh")){
                    lan = "zh-CN";
                    currency = "RMB";
                }else{
                    lan = "en";
                    currency="USD";
                }

                OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
                params[0] = new OkHttpClientManager.Param("device_no", APIConstants.sn);
                params[1] = new OkHttpClientManager.Param("lang", lan);
                params[2] = new OkHttpClientManager.Param("currency", currency);

                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), getResources().getString(R.string.loading));
                new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);
                APIConstants.isScanNewDevice = false;
            }
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                return;
            }
            if(msg.what == -10){
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                return;
            }

            DeviceOrderHttpResponseData responseData = new DeviceOrderHttpResponseData((String) msg.obj);
            if (responseData == null || responseData.data == null) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                return;
            }
            switch (msg.what){
                case 1:
                    Log.i("products:",responseData.data.toString());
                    ArrayList<LinkedTreeMap<String, Object>> mData = responseData.data;
                    if(mData != null){
                        flowProductInfoList = new ArrayList<FlowProductInfo>();
                        for(int i = 0; i < mData.size(); i++){
                            FlowProductInfo flowProduct = new FlowProductInfo(mData.get(i));
                            if (flowProduct.productname.equals("") || flowProduct.price.equals("") ||
                            flowProduct.priceType.equals("") || flowProduct.productid.equals("")){

                                continue;
                            }
                            flowProductInfoList.add(flowProduct);
                            Log.e("nammmmmme", flowProduct.producttype);
                        }
                        flowMallAdapter = new FlowMallAdapter(getActivity(), flowProductInfoList);
                        flowMallAdapter.setOnItemClickListener(new FlowMallAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                Intent intent = new Intent(getActivity(), FlowProductDetailActivity.class);
                                intent.putExtra("product_id", flowProductInfo.productid);
                                intent.putExtra("url", flowProductInfo.url);
                                intent.putExtra("price", flowProductInfo.price);
                                intent.putExtra("price_type", flowProductInfo.priceType);
                                intent.putExtra("title", flowProductInfo.productname);
                                intent.putExtra("total_flow", flowProductInfo.total_flow);
                                intent.putExtra("coverage", flowProductInfo.coverage);
                                intent.putExtra("effective_days", flowProductInfo.effective_days);
                                intent.putExtra("notice", flowProductInfo.notice);
                                intent.putExtra("coverImage", flowProductInfo.coverImage);
                                intent.putExtra("productnumber", flowProductInfo.productnumber);
                                intent.putExtra("activedays", flowProductInfo.activedays);
                                intent.putExtra("producttype", flowProductInfo.producttype);


                                Log.i("urlurlulr:",APIConstants.server_host + flowProductInfo.url+flowProductInfo.price);

                                intent.putExtra("position", position);
                                getActivity().startActivity(intent);
                            }
                        });
                        rv_flow_package.setAdapter(flowMallAdapter);
                        }
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
                default:
                    break;
            }
        }
    }
}
