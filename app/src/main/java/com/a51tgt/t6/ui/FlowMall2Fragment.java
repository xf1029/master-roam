package com.a51tgt.t6.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.adapter.simpleAdapter;
import com.a51tgt.t6.bean.AreaInfo;
import com.a51tgt.t6.bean.HttpResponseData;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.net.OkHttpClientManager;
import com.a51tgt.t6.net.SendRequest;
import com.a51tgt.t6.ui.view.ImageCarousel;
import com.a51tgt.t6.ui.view.ImageInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FlowMall2Fragment extends Fragment {

    String TAG = "FlowMall2Fragment";

    private RecyclerView rv_area_package;
    private List<String> flowProductInfoList1;
    private simpleAdapter adapter;
    private List<Map<String, Object>> mData;
    private int width;
    View root;
//    private RelativeLayout rl_fragment_fole_category;




    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点

    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;


    public FlowMall2Fragment() {
    }

    public static FlowMall2Fragment newInstance() {
        FlowMall2Fragment fragment = new FlowMall2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String runningOnCreate = "Running OnCreate";
        Log.e(TAG, runningOnCreate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View rootView = inflater.inflate(R.layout.fragment_flow_mall_2, container, false);
        root = rootView;
        rv_area_package = rootView.findViewById(R.id.recycleView);

        WindowManager wm1 = getActivity().getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        width =(width1-36)/2;
        initView(rootView);
        initEvent();
        imageStart();

//        rl_fragment_fole_category = rootView.findViewById(R.id.rl_fragment_fole_category);
//        rl_fragment_fole_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), SearchFlowActivity.class));
//            }
//        });

//        String s1 = "1";
//        for (int i = 0; i < 8; i++){
//            flowProductInfoList1.add(s1);
//        }

        adapter = new simpleAdapter(flowProductInfoList1, getContext(), width);
        rv_area_package.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //        int size = flowProductInfoList1.size();
                int size = 8;
                if (position == size-1 && size%2 == 1){
                    return 2;
                }else {
                    return 1;
                }
            }
        });

        rv_area_package.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(new simpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AreaInfo areaInfo = new AreaInfo(mData.get(position));
                Log.i("postion:", areaInfo.title);
                skip(areaInfo.group_name, areaInfo.title);
            }
        });


        PackageManager packageManager = getActivity().getPackageManager();

        PackageInfo packInfo = null;
        try {
            String str = getContext().getPackageName();

            packInfo = packageManager.getPackageInfo(str,0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String lang;
        if (Locale.getDefault().getLanguage().contains("ja")){
            lang = "jp";

        }else if (Locale.getDefault().getLanguage().contains("zh")){
            lang = "zh";

        }else {

            lang ="en";
        }
        String version = packInfo.versionName;
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[2];
        params[0] = new OkHttpClientManager.Param("lang", lang);
        params[1] = new OkHttpClientManager.Param("ver",  version);

        new SendRequest(APIConstants.Get_Flow_MallGroup, params, new MyHandler(), 1);

        return rootView;
    }

    public void skip(String name, String title){
        Intent intent = new Intent(getActivity(), FlowActivity.class);
//        intent.putExtra("areaType", name);
//        intent.putExtra("areaTitle", title);
//        intent.putExtra("areaType",i);
        startActivity(intent);
    }
    /**
     * 初始化事件
     */
    private void initEvent() {
        imageInfoList = new ArrayList<>();
        imageInfoList.add(new ImageInfo(1, "图片1，公告1啦啦啦啦，陆欢博客", "", "http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834a75bb01156b5c9ea15cebf2f.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片2，公告2啦啦啦啦，陆欢博客", "", "http://c.hiphotos.baidu.com/image/h%3D300/sign=cfce96dfa251f3dedcb2bf64a4eff0ec/4610b912c8fcc3ce912597269f45d688d43f2039.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片3，公告3啦啦啦啦，陆欢博客", "", "http://e.hiphotos.baidu.com/image/pic/item/6a600c338744ebf85ed0ab2bd4f9d72a6059a705.jpg", "http://www.cnblogs.com/luhuan/"));
        imageInfoList.add(new ImageInfo(1, "图片4，公告4啦啦啦啦，陆欢博客", "仅展示", "http://b.hiphotos.baidu.com/image/h%3D300/sign=8ad802f3801001e9513c120f880e7b06/a71ea8d3fd1f4134be1e4e64281f95cad1c85efa.jpg", ""));
        imageInfoList.add(new ImageInfo(1, "图片5，公告5啦啦啦啦，陆欢博客", "仅展示", "http://e.hiphotos.baidu.com/image/h%3D300/sign=73443062281f95cab9f594b6f9177fc5/72f082025aafa40fafb5fbc1a664034f78f019be.jpg", ""));


    }

    /**
     * 初始化控件
     */
    private void initView(View rootview) {

        mViewPager = rootview.findViewById(R.id.viewPager);
        Log.i("mvvvv",mViewPager.toString());
        mTvPagerTitle =rootview.findViewById(R.id.tv_pager_title);
        mLineLayoutDot =rootview. findViewById(R.id.lineLayout_dot);

    }

    private void imageStart() {
        //设置图片轮播
        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,
                R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
        String[] titles = new String[imageInfoList.size()];
        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

        for (int i = 0; i < imageInfoList.size(); i++) {
            titles[i] = imageInfoList.get(i).getTitle();
            Log.i("getconteg",getContext().toString());
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getContext());
            simpleDraweeView.setAspectRatio(1.78f);
            // 设置一张默认的图片
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(this.getResources())
                    .setPlaceholderImage(ContextCompat.getDrawable(getActivity(), R.drawable.default_img), ScalingUtils.ScaleType.CENTER_CROP).build();
            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            //加载高分辨率图片;
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                    .setResizeOptions(new ResizeOptions(1280, 720))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    //.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);

            simpleDraweeView.setId(imgaeIds[i]);//给view设置id
            simpleDraweeView.setTag(imageInfoList.get(i));
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(mLineLayoutDot, fromResToDrawable(getActivity(), R.drawable.ic_dot_focused), simpleDraweeViewList.size());
        imageCarousel = new ImageCarousel(getActivity(), mViewPager, mTvPagerTitle, dots, 5000);
        imageCarousel.init(simpleDraweeViewList, titles)
                .startAutoPlay();
        imageCarousel.start();

    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return 小点的Id
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getActivity());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        ((getActivity())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
               linearLayout.addView(dot);
            }
        });

        return dot.getId();
    }


    /**
     * 资源图片转Drawable
     *
     * @param context 上下文
     * @param resId   资源ID
     * @return 返回Drawable图像
     */
    public static Drawable fromResToDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
        //return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout 线性横向布局
     * @param backgount    小点资源图标
     * @param number       数量
     * @return 返回小点View集合
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(root.findViewById(dotId));

        }
        return dots;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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

            HttpResponseData responseData = new HttpResponseData((String) msg.obj);
            if (responseData == null || responseData.status < 0 || responseData.data == null) {
                return;
            }

            switch (msg.what){
                case 1:
                    Log.i("product:",responseData.data.toString());
                    flowProductInfoList1 = new ArrayList<String>();
                    if(responseData.data.containsKey("group_list")){
                        mData = (List<Map<String, Object>>)responseData.data.get("group_list");
                        if(mData != null){
                            for(int i = 0; i < mData.size(); i++){
                                AreaInfo areaInfo = new AreaInfo(mData.get(i));
                                flowProductInfoList1.add(areaInfo.img_url);
                            }
//                            flowProductInfoList1.add(flowProductInfoList1.get(0));
//                            flowProductInfoList1.add(flowProductInfoList1.get(1));
//
//                            flowProductInfoList1.add(flowProductInfoList1.get(0));
//                            flowProductInfoList1.add(flowProductInfoList1.get(1));


                            adapter = new simpleAdapter(flowProductInfoList1, getContext(), width);
//        recycler.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
//        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                            rv_area_package.setAdapter(adapter);

//                            flowMallAdapter = new FlowMallAdapter(getActivity(), flowProductInfoList);

                            adapter.setOnItemClickListener(new simpleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    AreaInfo areaInfo = new AreaInfo(mData.get(position));

                                    Log.i("postion:", areaInfo.title);
                                    skip(areaInfo.group_name, areaInfo.title);

                                }
                            });

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position == flowProductInfoList1.size()-1&&flowProductInfoList1.size()%2==1){
                                        return 2;
                                    }else {
                                        return 1;
                                    }
                                }
                            });

                            rv_area_package.setLayoutManager(gridLayoutManager);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
