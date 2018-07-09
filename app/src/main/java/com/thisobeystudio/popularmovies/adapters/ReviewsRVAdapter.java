package com.thisobeystudio.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.models.Review;

import java.util.List;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class ReviewsRVAdapter extends RecyclerView.Adapter<ReviewsRVAdapter.ReviewViewHolder> {


    private final List<Review> reviews;

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView reviewAuthor;
        final TextView reviewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewContent = itemView.findViewById(R.id.review_content);
        }
    }

    public ReviewsRVAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder viewHolder, int i) {
        viewHolder.reviewAuthor.setText(reviews.get(i).getAuthor());
        viewHolder.reviewContent.setText(reviews.get(i).getContent());
    }

}