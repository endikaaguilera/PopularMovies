package com.thisobeystudio.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.thisobeystudio.popularmovies.activities.MainActivity;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.objects.GridViewImageView;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

import static android.widget.ImageView.ScaleType.FIT_XY;

/**
 * Created by thisobeystudio on 29/7/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public final class GridViewAdapter extends BaseAdapter {

    private final Context context;
    private Cursor cursor;

    public GridViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (cursor != null) {

            GridViewImageView view = (GridViewImageView) convertView;
            if (view == null) {
                view = new GridViewImageView(context);
                view.setScaleType(FIT_XY);
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.border_shape));
            }

            // Get the image URL for the current position.
            final String path = getItem(position);

            Glide.with(context).load(NetworkUtils.IMAGES_URL + path).into(view);

            return view;

        } else return null;

    }

    @Override
    public int getCount() {
        if (cursor != null) {
            cursor.moveToPosition(0);
            return cursor.getCount();
        } else return 0;
    }

    @Override
    public String getItem(int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            return cursor.getString(MainActivity.MAIN_TABLE_INDEX_COLUMN_POSTER_PATH);
        } else return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * swpap cursor
     *
     * @param newCursor new cursor
     */
    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

}
