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
import android.view.ViewTreeObserver;
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
import com.a51tgt.t6.ui.view.CustomGridLayoutManager;
import com.a51tgt.t6.ui.view.ObservableScrollView;
import com.a51tgt.t6.utils.CommUtil;
import com.a51tgt.t6.utils.GlideUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ganxin.library.LoadDataLayout;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FlowActivity extends BaseActivity implements ObservableScrollView.OnObservableScrollViewListener{
    RecyclerView rv_daily;
//            , rv_monthly, rv_multiplemonths;



    private ObservableScrollView mObservableScrollView;
    private ImageView mTextView;
    private int mHeight;
    int position1=0,  position2=0;
    private String[] groupNames = {"A", "B", "C", "D", "E", "F", "G"};


    NewFlowMallAdapter flowMallAdapter_daily, flowMallAdapter_monthly,
            flowMallAdapter_multiplemonths;

    List<String> list = new ArrayList<>();

    private List<Map<String, Object>> mData;
    private List<FlowProductInfo> flowProductInfoList_daily = new ArrayList<>();
    private  List<List<FlowProductInfo>>  total = new ArrayList<>();
    private List<FlowProductInfo> flowProductInfoList_monthly = new ArrayList<>();
    private List<FlowProductInfo> flowProductInfoList_multiplemonths = new ArrayList<>();

    ImageView back;
    LoadDataLayout loadDataLayout;
    //    ImageView iv_east_asin_flow_image;
    private String [] area = {"中国","东亚","亚洲","大洋洲","北美洲","欧洲","全球"};
    int areaType = -1;
    int width;
    String areaP,areaTitle;

    TextView tv_title;
    View emptyView;

    private LinearLayout mHeaderContent;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        tv_title = findViewById(R.id.tv_title);

        loadDataLayout = findViewById(R.id.loadDataLayout);
        loadDataLayout.setStatus(LoadDataLayout.LOADING); //加载中


        // 重新加载点击监听
        loadDataLayout.setOnReloadListener(new LoadDataLayout.OnReloadListener() {
            @Override
            public void onReload(View v, int status) {
                loadDataLayout.setStatus(LoadDataLayout.LOADING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                       getData(1);


                        // tv.setText(str); // 该句模拟有数据的情况，直接给TextView赋值
//                        loadDataLayout
//                                .setReloadBtnVisible(false) // 默认数据为空的时候也会显示重新加载按钮，此处设置不显示，具体视需求而定
////                                .setEmptyImage(R.mipmap.ic_launcher)  // 可以替换成自己的图片
//                                .setEmptyText("Sorry！该分类下数据暂时为空QwQ")
//                                .setStatus(LoadDataLayout.EMPTY);
                    }
                },2000);
            }
        });


        //初始化控件
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.srcoller);
        mTextView = (ImageView) findViewById(R.id.iv_bg);

        mHeaderContent = (LinearLayout) findViewById(R.id.ll_header_content);

        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = mTextView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = mTextView.getHeight() - mHeaderContent.getHeight();//这里取的高度应该为图片的高度-标题栏
                //注册滑动监听
                mObservableScrollView.setOnObservableScrollViewListener(FlowActivity.this);
            }
        });










//        iv_east_asin_flow_image = findViewById(R.id.iv_east_asin_flow_image);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //去除半透明状态栏
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //全屏显示
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        WindowManager wm1 = getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        width =(width1-36)/2;

        back = findViewById(R.id.iv_header_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv_daily = findViewById(R.id.rv_daily);
        rv_daily.setNestedScrollingEnabled(true);


//        rv_monthly = findViewById(R.id.rv_monthly);
//
//        rv_multiplemonths = findViewById(R.id.rv_multiplemonths);

        areaP = getIntent().getStringExtra("areaType");
        areaTitle = getIntent().getStringExtra("areaTitle");

        if (!TextUtils.isEmpty(areaP)&& areaP.length()>0){
            tv_title.setText(areaTitle);
        } else {
//            Intent intent = getIntent();
//            String searchkey = intent.getStringExtra("searchkey");
//            tv_title.setText(searchkey);
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
        httprequest(currency,lan);


    }
    private  void  httprequest(String currency,String lan){

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
        params[0] = new OkHttpClientManager.Param("device_no",  APIConstants.sn);
        params[1] = new OkHttpClientManager.Param("currency", currency);
        params[2] = new OkHttpClientManager.Param("lang", lan);

        new SendRequest(APIConstants.Get_Flow_Mall, params, new MyHandler(), 1);

    }

    public void enter_this_product(FlowProductInfo flowProductInfo){
        Intent intent = new Intent(this, DetailActivity.class);

//        intent.putExtra("pruducInfo",flowProductInfo);
        intent.putExtra("product_id", flowProductInfo.productid);
        intent.putExtra("price", flowProductInfo.price);
        intent.putExtra("price_type", flowProductInfo.priceType);
        intent.putExtra("title", flowProductInfo.productname);
        intent.putExtra("total_flow", flowProductInfo.total_flow);
        intent.putExtra("coverage", flowProductInfo.coverage);
        intent.putExtra("effective_days", flowProductInfo.effective_days);
        intent.putExtra("notice", flowProductInfo.notice);
        intent.putExtra("productnumber", flowProductInfo.productnumber);
        intent.putExtra("activedays", flowProductInfo.activedays);
        intent.putExtra("producttype", flowProductInfo.producttype);

        startActivity(intent);
    }



    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            Log.i("FFFFFF",String.valueOf(msg.what));


            if (msg == null || msg.obj == null) {
                return;
            }
            if(msg.what == -10){
                return;
            }

            DeviceOrderHttpResponseData responseData = new DeviceOrderHttpResponseData((String) msg.obj);
            if (responseData == null || responseData.data == null) {


                loadDataLayout.setStatus(LoadDataLayout.NO_NETWORK);
                return;
            }
            loadDataLayout.setVisibility(View.GONE);

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



//                                                            flowProductInfoList_daily.add(flowProduct);



                            if (flowProduct.producttype.contains("DAILY")){
                                flowProduct.setText("日租包");
                                flowProduct.setTextcolor(R.color.color_flow);
                                flowProductInfoList_daily.add(flowProduct);

                            } else if (flowProduct.producttype.contains("MONTH")){
                                flowProduct.setText("月租包");

                                flowProduct.setTextcolor(getResources().getColor(R.color.color_month));

                                flowProductInfoList_monthly.add(flowProduct);
                            }else{
//                                flowProduct.setTextcolor(getResources().getColor(R.color.color_flow));
//
//                                flowProductInfoList_multiplemonths.add(flowProduct);
                            }
                        }


//                        flowProductInfoList_daily.add(0,null);

                        total.add(flowProductInfoList_daily);

                        total.add(flowProductInfoList_monthly);
//                        total.add(flowProductInfoList_multiplemonths);


                        for (int i=0;i<total.size();i++){

                            if (i==1){

                                position1=total.get(0).size()+1;

                            }else if (total.size()==3){



                                    position2=total.get(0).size()+ total.get(1).size()+2;
                            }



                        }
//                        total.add(2,flowProductInfoList_daily);
//                    flowProductInfoList_daily.add()

//                        flowMallAdapter_daily = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_daily);                        flowMallAdapter_daily = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_daily);

                        flowMallAdapter_daily = new NewFlowMallAdapter(FlowActivity.this, total);

//                        flowMallAdapter_monthly = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_monthly);
//                        flowMallAdapter_multiplemonths = new NewFlowMallAdapter(FlowActivity.this, flowProductInfoList_multiplemonths);
                        flowMallAdapter_daily.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
                                enter_this_product(flowProductInfo);
                            }
                        });
//                        flowMallAdapter_monthly.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
//                                enter_this_product(flowProductInfo);
//                            }
//                        });
//                        flowMallAdapter_multiplemonths.setOnItemClickListener(new NewFlowMallAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position, FlowProductInfo flowProductInfo) {
//                                enter_this_product(flowProductInfo);
//                            }
//                        });
                        rv_daily.setAdapter(flowMallAdapter_daily);
//                        rv_monthly.setAdapter(flowMallAdapter_monthly);
//                        rv_multiplemonths.setAdapter(flowMallAdapter_multiplemonths);

                        CustomGridLayoutManager gridLayoutManager_daily = new CustomGridLayoutManager(FlowActivity.this,2);
Boolean srco = gridLayoutManager_daily.canScrollVertically();
gridLayoutManager_daily.setScrollEnabled(false);
          Log.i("canscoler",srco.toString());
                        gridLayoutManager_daily.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {



//                                List<FlowProductInfo> LIST = total.get(position);

//                                return 2;


                                if (position==0||position==position1||position==position2){
                                    return 2;
                                }else {

                                    return 1;
                                }

//                                return 1;
//                                if (position == flowProductInfoList_daily.size()-1&&flowProductInfoList_daily.size()%2==1){
//                                    return 2;
//                                }else {
//                                    return 2;
//                                }
                            }
                        });

                        GridLayoutManager gridLayoutManager_monthly = new GridLayoutManager(FlowActivity.this,2);
                        gridLayoutManager_monthly.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return 1;
//                                if (position == flowProductInfoList_monthly.size()-1&&flowProductInfoList_monthly.size()%2==1){
//                                    return 2;
//                                }else {
//                                    return 2;
//                                }
                            }
                        });

                        GridLayoutManager gridLayoutManager_multiplemonths = new GridLayoutManager(FlowActivity.this,2);
                        gridLayoutManager_multiplemonths.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {

                                return 1;
//                                if (position == flowProductInfoList_multiplemonths.size()-1&&flowProductInfoList_multiplemonths.size()%2==1){
//                                    return 2;
//                                }else {
//                                    return 2;
//                                }
                            }
                        });

                        rv_daily.setLayoutManager(gridLayoutManager_daily);
//                        rv_monthly.setLayoutManager(gridLayoutManager_monthly);
//                        rv_multiplemonths.setLayoutManager(gridLayoutManager_multiplemonths);

                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //顶部图处于最顶部，标题栏透明
            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
        } else if (t > 0 && t < mHeight) {
            //滑动过程中，渐变
            float scale = (float) t / mHeight;//算出滑动距离比例
            float alpha = (255 * scale);//得到透明度
            mHeaderContent.setBackgroundColor( getResources().getColor(R.color.baseColorLight));

            mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 16, 181, 255));
        } else {
            //过顶部图区域，标题栏定色
//            mHeaderContent.setBackgroundColor( getResources().getColor(R.color.baseColorLight));
            mHeaderContent.setBackgroundColor(Color.argb(255, 16, 181, 255));
        }
    }


}
