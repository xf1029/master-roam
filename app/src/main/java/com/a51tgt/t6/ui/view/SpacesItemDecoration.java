package com.a51tgt.t6.ui.view;

/**
 * Created by liu_w on 2018/1/30.
 */

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager spacing
 * Created by john on 17-1-5.
 */

/**
 * 设置RecyclerView LinearLayoutManager spacing
 * Created by john on 17-1-5.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}

