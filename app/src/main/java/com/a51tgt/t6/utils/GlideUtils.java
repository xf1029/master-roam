package com.a51tgt.t6.utils;

import android.content.Context;
import android.widget.ImageView;

import com.a51tgt.t6.R;
import com.bumptech.glide.Glide;


/**
 * Created by Administrator on 2017/7/25.
 */

public class GlideUtils {
    public static void load(Context context, String url, ImageView img) {
        Glide.with(context).load(url).error(R.mipmap.ic_icon).into(img);
    }

    public static void load(Context context, int resourceId, ImageView img) {
        Glide.with(context).load(resourceId).error(R.mipmap.ic_icon).into(img);
    }

    public static void loadPhoto(Context context, String url, ImageView img) {
        Glide.with(context).load(url).error(R.mipmap.ic_icon).into(img);
    }

    public static void loadCrop(Context context, String url, ImageView img) {
        Glide.with(context).load(url).error(R.mipmap.ic_icon).centerCrop().into(img);
    }

    public static void loadFitCenter(Context context, String url, ImageView img) {
        Glide.with(context).load(url).error(R.mipmap.ic_icon).fitCenter().into(img);
    }

    public static void loadFitCenter(Context context, int resourceId, ImageView img) {
        Glide.with(context).load(resourceId).error(R.mipmap.ic_icon).fitCenter().into(img);
    }
}
