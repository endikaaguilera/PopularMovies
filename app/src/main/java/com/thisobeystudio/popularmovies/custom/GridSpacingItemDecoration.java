package com.thisobeystudio.popularmovies.custom;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thisobeystudio on 12/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing;

    public GridSpacingItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        // item position
        int position = parent.getChildAdapterPosition(view);
        // item column
        int column = position % spanCount;

        // spacing - column * ((1f / spanCount) * spacing)
        outRect.left = spacing - column * spacing / spanCount;
        // (column + 1) * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount;

        if (position < spanCount) {
            // item top
            outRect.top = spacing;
        }

    }

}
