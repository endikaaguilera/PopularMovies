package com.thisobeystudio.popularmovies.objects;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by thisobeystudio on 12/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

// this class determines the trailer image aspect >>> width & height
public class TrailerImage extends android.support.v7.widget.AppCompatImageView {

    public TrailerImage(Context context) {
        super(context);
    }

    public TrailerImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width - width / 4;
        setMeasuredDimension(width, height);
    }

}