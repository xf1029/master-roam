package com.a51tgt.t6.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a51tgt.t6.R;

import java.util.ArrayList;
import java.util.List;


public class simpleAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private List<String> myData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    int size = 8;

    private int width1;
    List<Integer>  mHeights;

    private int[] pic = new int[]{R.drawable.japan, R.drawable.east_asia, R.drawable.asia,
            R.drawable.europe, R.drawable.north_america, R.drawable.oceania,
            R.drawable.global, R.drawable.other};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_category, parent,false);

        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        return viewHolder;
    }

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ImageView image = (ImageView) holder.itemView.findViewById(R.id.id_category_pic);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();


        image.setImageResource(pic[position]);
//        GlideUtils.load(mContext, APIConstants.server_host + myData.get(position), image);

        if(position != getItemCount()-1){
//          layoutParams.width = width1 ;
            Log.e("position", String.valueOf(position));
            Log.e("width:",String.valueOf(width1));
            Log.e("height:", String.valueOf(layoutParams.height));
//          layoutParams.height = (int) ((int) width1*0.75);

//            image.setLayoutParams(layoutParams);
//          textView.setText(myData.get(position));
        }else{
//          layoutParams.width = width1*2+36 ;
//          layoutParams.height = (int) (layoutParams.width*0.3);
            Log.e("position111", String.valueOf(position));
            Log.e("width1111:",String.valueOf(width1));
            Log.e("height1111:", String.valueOf(layoutParams.height));
//            image.setLayoutParams(layoutParams);
        }

//        image.setTag(position);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return size;
//        return myData.size();
    }

    public simpleAdapter(List<String> myData, Context mContext, int width) {
        this.myData = myData;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        mHeights = new ArrayList<>();
        width1 = width;
//        for (int i=0; i < myData.size(); i++){
        for(int i = 0; i < size; i++){
            mHeights.add(i, (int)(Math.random() * 100));
        }

    }
}
