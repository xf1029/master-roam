package com.a51tgt.t6.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.FlowProductInfo;
import com.a51tgt.t6.comm.APIConstants;
import com.a51tgt.t6.utils.ImageUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NewFlowMallAdapter extends  RecyclerView.Adapter {

//    private List<FlowProductInfo> data;
private List<List<FlowProductInfo>> data;

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext;

    private final int HEAD = 0;
    private final int ITEM = 1;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position, FlowProductInfo flowProductInfo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public NewFlowMallAdapter(Context context, List<List<FlowProductInfo>> data) {
        mContext = context;
        this.data = data;
    }

//    public NewFlowMallAdapter(Context context, List<FlowProductInfo> data) {
//        mContext = context;
//        this.data = data;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View itemView = inflater.inflate(R.layout.item_flow_product_02, null);
//        final ItemViewHolder viewHolder = new ItemViewHolder(itemView);
//        return viewHolder;

        if (viewType == HEAD) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_flow_product_header, null);
            HeadViewHolder viewHolder = new HeadViewHolder(itemView);
            return viewHolder;

        }else {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_flow_product_02, null);
         ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;



        }

//        return null;
        }

    @Override
    public int getItemViewType(int position) {
        int count=0;
        Log.i("postion:",String.valueOf(position));
        for(int i = 0; i < data.size(); i++){
            if(position==count){

                count++;

                return HEAD;
            }
            List<FlowProductInfo> addressList = data.get(i);
            for(int j =0;j<addressList.size();j++){
                count++;
                if(position==count){
                    return ITEM;
                }

            }
            count++;
        }


//        int count=0;
//        for(int i = 0; i < data.size(); i++){
////            count++;
//            if(position==count){
//                return HEAD;
//            }
//            count++;
//            List<FlowProductInfo> addressList = data.get(i);
//            for(int j =0;j<addressList.size();j++){
//
//                return ITEM;
////                count++;
////                if(position==count){
////                    return ITEM;
////                }
//            }
//        }
              return super.getItemViewType(position);


    }
//    @Override
//    public int getItemViewType(int position) {
//
//        int count = 0;
//        if (position == count) return HEAD;//下标为0的固定显示头部布局。
//
////        return  ITEM;
//        for (int i = 0; i < data.size(); i++) {
//            count++;
//            if (position == count) {
//                return ITEM;
//            }
//        }
//       return super.getItemViewType(position);
//
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//
//
//        final FlowProductInfo item = data.get(position);
//        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//        itemViewHolder.tv_title.setText(item.productname);
//        //itemViewHolder.tv_subtitle.setText(item.subTitle);
////    double   d   =   Double.parseDouble(item.price);
////    int i = (int) d ;
//
////    int price=0;
////         price = Integer.parseInt(item.price);
////    String[] price = item.price.split(".");
//        itemViewHolder.tv_price.setText(item.price);
//        itemViewHolder.tv_price_type.setText(item.priceType);
//        Picasso mPicasso = Picasso.with(mContext);
//        mPicasso.setIndicatorsEnabled(false);
////        if (!item.coverImage.equals("")) {
////            mPicasso.load(APIConstants.server_host + item.coverImage)
////                    .into(itemViewHolder.iv_cover, new Callback() {
////                        @Override
////                        public void onSuccess() {
////                            Bitmap bitmap = ImageUtils.drawableToBitmap(itemViewHolder.iv_cover.getDrawable());
//////                android.view.ViewGroup.LayoutParams para;
//////                para = itemViewHolder.iv_cover.getLayoutParams();
//////                float height = (float) para.width * ((float)bitmap.getHeight() / (float) bitmap.getWidth());
//////                para.height = (int)height;
//////                itemViewHolder.iv_cover.setLayoutParams(para);
////                            //ImageViewUtil.matchAll(mContext, itemViewHolder.iv_cover);
////                        }
////
////                        @Override
////                        public void onError() {
////
////                        }
////                    });
////        }
//        itemViewHolder.itemView.setTag(position);
//
//        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    //注意这里使用getTag方法获取position
//                    mOnItemClickListener.onItemClick(v,(int)v.getTag(), data.get(position));
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        if(data == null)
//            return 0;
//        else
//            return data.size();
//
//    }
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        int count = 0;
//        for (int i = 0; i < data.size(); i++) {
////            count += 1;
//            if (holder.getItemViewType() == HEAD) {
//                HeadViewHolder viewHolder = (HeadViewHolder) holder;
//                viewHolder.tv_subtitle.setText("测试");
//            } else {
//
//                final ArrayList<FlowProductInfo> item = data.get(position-1);
//
//                final FlowProductInfo item = data.get(position-1);
//
////                final FlowProductInfo item = data.get(position-1);
//        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//        itemViewHolder.tv_title.setText(item.productname);
//        itemViewHolder.tv_price.setText(item.price);
//        itemViewHolder.tv_price_type.setText(item.priceType);
//        Picasso mPicasso = Picasso.with(mContext);
//        mPicasso.setIndicatorsEnabled(false);
////        if (!item.coverImage.equals("")) {
////            mPicasso.load(APIConstants.server_host + item.coverImage)
////                    .into(itemViewHolder.iv_cover, new Callback() {
////                        @Override
////                        public void onSuccess() {
////                            Bitmap bitmap = ImageUtils.drawableToBitmap(itemViewHolder.iv_cover.getDrawable());
//////                android.view.ViewGroup.LayoutParams para;
//////                para = itemViewHolder.iv_cover.getLayoutParams();
//////                float height = (float) para.width * ((float)bitmap.getHeight() / (float) bitmap.getWidth());
//////                para.height = (int)height;
//////                itemViewHolder.iv_cover.setLayoutParams(para);
////                            //ImageViewUtil.matchAll(mContext, itemViewHolder.iv_cover);
////                        }
////
////                        @Override
////                        public void onError() {
////
////                        }
////                    });
////        }
//        itemViewHolder.itemView.setTag(position);
//
//        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    //注意这里使用getTag方法获取position
//                    mOnItemClickListener.onItemClick(v,(int)v.getTag(), data.get(position));
//                }
//            }
//        });
//
//
//            }
//        }
//
//
//
//
//    }

        @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            int count = 0;
            int flag=0;
            for (int i = 0; i < data.size(); i++) {

                if (position == count) {



                    List<FlowProductInfo> templist = data.get(flag);
                    FlowProductInfo item1 = templist.get(0);
                    final HeadViewHolder header = (HeadViewHolder) holder;
                    Log.i("item1",item1.getText());

                    if (flag==0){


                         header.tv_subtitle.setText("日租包");

                        header.tv_subtitle.setTextColor(mContext.getResources().getColor(R.color.color_dayily));

                    }else if (flag==1){
                        header.tv_subtitle.setText("月租包");
                        header.tv_subtitle.setTextColor(mContext.getResources().getColor(R.color.color_month));

//
//
                    }

//
//                header.tv_subtitle.setText(item1.getText());
//                    header.tv_subtitle.setTextColor(item1.getTextcolor());


//                    if (i==0) {
//                        header.tv_subtitle.setText("日租包");
//
//                        header.tv_subtitle.setTextColor(mContext.getResources().getColor(R.color.color_dayily));
//                    }else if (i==1){
//                        header.tv_subtitle.setText("月租包");
//                        header.tv_subtitle.setTextColor(mContext.getResources().getColor(R.color.color_month));
//
//
//
//                    }else if (i==data.size()-1){
//
//                        header.tv_subtitle.setText("加油包");
//                        header.tv_subtitle.setTextColor(mContext.getResources().getColor(R.color.color_flow));
//
//
//
//                    }
                } else {
                    List<FlowProductInfo> addressList = data.get(i);
                    for (int j = 0; j < addressList.size(); j++) {
                        count += 1;
                        if (position == count) {


                            final FlowProductInfo item = addressList.get(j);

//                for (int j = 0; j < item1.size(); j++) {
                            Log.i("postion22:", String.valueOf(position));


                            Log.i("postion22:", String.valueOf(position - 1));


                            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                            itemViewHolder.tv_title.setText(item.productname);
                            itemViewHolder.tv_price.setText(item.price);
                            itemViewHolder.tv_price_type.setText(item.priceType);
                            String use_day = item.effective_days+mContext.getString(R.string.day);
                            itemViewHolder.tv_use_day.setText(use_day);
                            if (flag==1){
                                itemViewHolder.tv_use_day.setBackground(mContext.getResources().getDrawable(R.drawable.effiect_day_month));

                            }
                            Picasso mPicasso = Picasso.with(mContext);
                            mPicasso.setIndicatorsEnabled(false);
//
                            itemViewHolder.itemView.setTag(position);

                            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mOnItemClickListener != null) {
                                        //注意这里使用getTag方法获取position
                    mOnItemClickListener.onItemClick(v,(int)v.getTag(),item);
                                    }
                                }
                            });


                        }
                    }
                    count++;
                    flag++;


                }


            }
        }


        @Override
    public int getItemCount() {
        if(data == null)
            return 0;
            int childCount = data.size();//字母的数量
            for (int i = 0; i < data.size(); i++) {
                childCount += data.get(i).size();//地区的数量
            }
            return childCount ;

    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        public View convertView;
        public TextView tv_title, tv_subtitle, tv_price, tv_price_type;
        public ImageView iv_cover;

        public HeadViewHolder(View convertView) {
            super(convertView);
            this.convertView = convertView;
            tv_subtitle = (TextView) convertView.findViewById(R.id.tv_su_title);

        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public View convertView;
        public TextView tv_title, tv_use_day, tv_price, tv_price_type;
        public ImageView iv_cover;

        public ItemViewHolder(View convertView) {
            super(convertView);
            this.convertView = convertView;
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_use_day = (TextView) convertView.findViewById(R.id.tv_use_day);
            tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            tv_price_type = (TextView) convertView.findViewById(R.id.tv_price_type);
//            iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
        }
    }

}