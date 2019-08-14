package com.a51tgt.t6.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class RecyclerviewAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public RecyclerviewAdapter(@LayoutRes int layoutResId, List<T> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (onCallBackData != null) {
            onCallBackData.convertView(helper, item);
        }
    }

    OnCallBackData onCallBackData;

    public void setOnCallBackData(OnCallBackData onCallBackData) {
        this.onCallBackData = onCallBackData;
    }

    public interface OnCallBackData<T> {
        void convertView(BaseViewHolder helper, T item);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}