package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.NewFlowMallAdapter;
import com.a51tgt.t6.bean.DeviceOrderHttpResponseData;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.GlideUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FlowActivity extends BaseActivity {
    RecyclerView rv_daily, rv_monthly, rv_multiplemonths;

    NewFlowMallAdapter flowMallAdapter_daily, flowMallAdapter_monthly,
            flowMallAdapter_multiplemonths;

    List<String> list = new ArrayList<>();

    private List<Map<String, Object>> mData;
    private List<FlowProductInfo> flowProductInfoList_daily = new ArrayList<>();
    private List<FlowProductInfo> flowProductInfoList_monthly = new ArrayList<>();
    private List<FlowProductInfo> flowProductInfoList_multiplemonths = new ArrayList<>();

    ImageView back;
    //    ImageView iv_east_asin_flow_image;
    private String [] area = {"中国","东亚","亚洲","大洋洲","北美洲","欧洲","全球"};
    int areaType = -1;
    int width;
    String areaP,areaTitle;

    TextView tv_title;
    View emptyView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        tv_title = findViewById(R.id.tv_title);
//        iv_east_asin_flow_image = findViewById(R.id.iv_east_asin_flow_image);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //去除半透明状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //全屏显示
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        WindowManager wm1 = getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        width =(width1-36)/2;

        back = findViewById(R.id.iv_east_asin_flow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv_daily = findViewById(R.id.rv_daily);

        rv_monthly = findViewById(R.id.rv_monthly);

        rv_multiplemonths = findViewById(R.id.rv_multiplemonths);

        areaP = getIntent().getStringExtra("areaType");
        areaTitle = getIntent().getStringExtra("areaTitle");

        if (!TextUtils.isEmpty(areaP)&& areaP.length()>0){
            tv_title.setText(areaTitle);
        } else {
            Intent intent = getIntent();
            String searchkey = intent.getStringExtra("searchkey");
            tv_title.setText(searchkey);
        }

        getData(areaType);
    }

    public void getData(int i){
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
        params[1] = new OkHttpClientManager.Param("currency", currency);
        params[2] = new OkHttpClientManager.Param("lang", lan);

        new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);
    }

    public void enter_this_product(FlowProductInfo flowProductInfo){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("product_id", flowProductInfo.productid);
        intent.putExtra("url", APIConstants.server_host + flowProductInfo.url);
        Log.i("urlurlulr:",APIConstants.server_host + flowProductInfo.url+flowProductInfo.price);
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

        startActivity(intent);
    }


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            if(msg.what == -10){
                return;
            }

            DeviceOrderHttpResponseData responseData = new DeviceOrderHttpResponseData((String) msg.obj);
            if (responseData == null || responseData.data == null) {
                return;
            }

            switch (msg.what){
                case 1:
                    Log.i("products:",responseData.data.toString());
                    ArrayList<LinkedTreeMap<String, Object>> mData = responseData.data;
                    if(mData != null){
                        flowProductInfoList_daily = new ArrayList<FlowProductInfo>();
                        flowProductInfoList_monthly = new ArrayList<FlowProductInfo>();
                        flowProductInfoList_multiplemonths = new ArrayList<FlowProductInfo>();
                        for(int i = 0; i < mData.size(); i++){
                            FlowProductInfo flowProduct = new FlowProductInfo(mData.get(i));
                            if (flowProduct.producttype.equals("DAILY")){
                                flowProductInfoList_daily.add(flowProduct);
                            } else if (flowProduct.producttype.equals("MONTH")){
                                flowProductInfoList_monthly.add(flowProduct);
                            }else{
                                flowProductInfoList_multiplemonths.add(flowProduct);
                            }
                        }
                        flowMallAdapter_daily = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_daily);
                        flowMallAdapter_monthly = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_monthly);
                        flowMallAdapter_multiplemonths = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_multiplemonths);
                        flowMallAdapter_daily.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                enter_this_product(flowProductInfo);
                            }
                        });
                        flowMallAdapter_monthly.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                enter_this_product(flowProductInfo);
                            }
                        });
                        flowMallAdapter_multiplemonths.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                enter_this_product(flowProductInfo);
                            }
                        });
                        rv_daily.setAdapter(flowMallAdapter_daily);
                        rv_monthly.setAdapter(flowMallAdapter_monthly);
                        rv_multiplemonths.setAdapter(flowMallAdapter_multiplemonths);

                        GridLayoutManager gridLayoutManager_daily = new GridLayoutManager(FlowActivity.this,2);
                        gridLayoutManager_daily.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (position == flowProductInfoList_daily.size()-1&&flowProductInfoList_daily.size()%2==1){
                                    return 2;
                                }else {
                                    return 1;
                                }
                            }
                        });

                        GridLayoutManager gridLayoutManager_monthly = new GridLayoutManager(FlowActivity.this,2);
                        gridLayoutManager_monthly.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (position == flowProductInfoList_monthly.size()-1&&flowProductInfoList_monthly.size()%2==1){
                                    return 2;
                                }else {
                                    return 1;
                                }
                            }
                        });

                        GridLayoutManager gridLayoutManager_multiplemonths = new GridLayoutManager(FlowActivity.this,2);
                        gridLayoutManager_multiplemonths.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (position == flowProductInfoList_multiplemonths.size()-1&&flowProductInfoList_multiplemonths.size()%2==1){
                                    return 2;
                                }else {
                                    return 1;
                                }
                            }
                        });

                        rv_daily.setLayoutManager(gridLayoutManager_daily);
                        rv_monthly.setLayoutManager(gridLayoutManager_monthly);
                        rv_multiplemonths.setLayoutManager(gridLayoutManager_multiplemonths);

                    }
                    break;
                default:
                    break;
            }
        }
    }
}
