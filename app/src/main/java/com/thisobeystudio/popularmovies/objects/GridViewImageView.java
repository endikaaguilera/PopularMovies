package com.thisobeystudio.popularmovies.objects;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by thisobeystudio on 30/7/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

// this class determines the GridView Images aspect >>> width & height
public final class GridViewImageView extends android.support.v7.widget.AppCompatImageView {

    public GridViewImageView(Context context) {
        super(context);
    }

    public GridViewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width + width / 2;
        setMeasuredDimension(width, height);
    }

}