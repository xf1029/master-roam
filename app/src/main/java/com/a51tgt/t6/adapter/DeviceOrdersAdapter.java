package com.a51tgt.t6.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a51tgt.t6.R;
import com.a51tgt.t6.bean.AcessPoint;
import com.a51tgt.t6.bean.PackageInfo;
import com.a51tgt.t6.ui.view.PackageInfoView;
import com.a51tgt.t6.ui.view.PackageInfoView2;
import com.a51tgt.t6.utils.CommUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;


public class DeviceOrdersAdapter extends RecyclerView.Adapter {

    private ArrayList<LinkedTreeMap<String, Object>> data;
    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position, String ssid);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public DeviceOrdersAdapter(Context context, ArrayList<LinkedTreeMap<String, Object>> data) {
        mContext = context;
        if (data == null) {
            this.data = new ArrayList<LinkedTreeMap<String, Object>>();
        } else {
            this.data = data;
        }
    }

    public void setData(ArrayList<LinkedTreeMap<String, Object>> data) {
        if (data == null) {
            this.data = new ArrayList<LinkedTreeMap<String, Object>>();
        } else {
            this.data = data;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_wifi_list, null);
        final ItemViewHolder viewHolder = new ItemViewHolder(new PackageInfoView2(mContext));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LinkedTreeMap<String, Object> item = data.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.view.setData(new PackageInfo(item));
        itemViewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }



    class ItemViewHolder extends RecyclerView.ViewHolder {

        public PackageInfoView2 view;

        public ItemViewHolder(PackageInfoView2 convertView) {
            super(convertView);
            this.view = convertView;
        }
    }

}
