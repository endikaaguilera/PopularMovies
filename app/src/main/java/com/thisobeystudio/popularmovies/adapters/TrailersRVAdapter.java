package com.thisobeystudio.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thisobeystudio.popularmovies.GlideApp;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.models.Trailer;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class TrailersRVAdapter extends RecyclerView.Adapter<TrailersRVAdapter.TrailerViewHolder> {

    private final Context context;
    private final List<Trailer> trailers;

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        final TextView trailerName;
        final TextView trailerSize;
        final ImageView trailerImageView;
        final ImageView trailerShareImageView;

        TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.trailer_name);
            trailerSize = itemView.findViewById(R.id.trailer_size);
            trailerImageView = itemView.findViewById(R.id.trailer_image);
            trailerShareImageView = itemView.findViewById(R.id.trailers_share_image);
        }
    }

    public TrailersRVAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_card, parent, false);
        return new TrailerViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder viewHolder, int i) {

        viewHolder.trailerName.setText(trailers.get(i).getName());
        String videoQuality = trailers.get(i).getSize() + "p";
        viewHolder.trailerSize.setText(videoQuality);

        final int pos = i;
        final String url = NetworkUtils.buildYoutubeThumbnailUrlString(trailers.get(i).getKey());
        GlideApp.with(context)
                .load(url)
                .into(viewHolder.trailerImageView);

        viewHolder.trailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = NetworkUtils.YOUTUBE_BASE_URL + trailers.get(pos).getKey();
                viewTrailer(url);

            }
        });

        viewHolder.trailerShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = NetworkUtils.YOUTUBE_BASE_URL + trailers.get(pos).getKey();
                shareTrailer(url);

            }
        });

    }

    /**
     *
     * launch new Intent.ACTION_VIEW to view selected trailer
     *
     * @param url trialer url
     */
    private void viewTrailer(String url) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }

    /**
     *
     * share selected trailer
     *
     * @param msg trailer content
     */
    private void shareTrailer(String msg) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_TEXT, msg);
        share.setType("text/*");
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share_intent_header)));
    }

}