package com.a51tgt.t6.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.ui.view.GridSpacingItemDecoration;
import com.a51tgt.t6.ui.view.RecyclerViewSpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountryActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CountryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        initView();
    }

    private void initView (){
        RecyclerView recyclerView = findViewById(R.id.country_rv);

        findViewById(R.id.iv_header_left).setOnClickListener(this);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
//        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
        CountryAdapter countryAdapter = new CountryAdapter();
//        recyclerView.setAdapter(countryAdapter);
//        recyclerView.addItemDecoration(new MyItemDecoration());




        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 50, true));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(countryAdapter);




        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,25);//top间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,50);//底部间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION,50);//左间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION,50);//右间距

        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header_left:
                finish();
                break;
        }
    }

    //设置ITEM之间的间距
    class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int i = parent.getChildLayoutPosition(view);
            outRect.left = 78;
            if (i % 4 == 0) {
                outRect.right = 39;
            } else if ((i + 1) % 4 == 0) {
                outRect.left = 39;
            } else {
                outRect.left = 39;
                outRect.right = 39;
            }
            outRect.bottom = 20;
        }
    }

    public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

        List<MyData> dataList = new ArrayList<>();
        public CountryAdapter (){
            Bitmap nationalFlag = BitmapFactory.decodeResource(getResources(), R.drawable.malaysia);
            Log.e(TAG, "CountryAdapter: " + nationalFlag );
            for (int i = 0; i < 40; i ++) {
                MyData data = new MyData();
                data.nationalFlag = nationalFlag;
                data.nationalName = "日本";
                dataList.add(data);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            MyData data = dataList.get(position);
            holder.tvNationalName.setText(data.nationalName);
            holder.ivNationalFlag.setImageBitmap(data.nationalFlag);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CountryActivity.this,FlowActivity.class);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder{



            ImageView ivNationalFlag;
            TextView tvNationalName;
            MyViewHolder(View itemView) {
                super(itemView);
                ivNationalFlag = itemView.findViewById(R.id.iv_national_flag);
                tvNationalName = itemView.findViewById(R.id.tv_national_name);
            }
        }
    }

    //需和界面进行绑定的数据
    class MyData {
        Bitmap nationalFlag;
        String nationalName;
    }
}
