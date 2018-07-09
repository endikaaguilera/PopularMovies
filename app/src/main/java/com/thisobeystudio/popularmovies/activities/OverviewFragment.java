package com.thisobeystudio.popularmovies.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.models.Movie;

/**
 * Created by thisobeystudio on 14/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class OverviewFragment extends Fragment {

    public OverviewFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static OverviewFragment newInstance(@NonNull Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INTENT_EXTRA_MOVIE, movie);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_overview, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOverViewDetails(view);
    }

    /**
     * setup overview card details
     *
     * @param view root view
     */
    private void setupOverViewDetails(View view) {

        if (getArguments() != null && getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE)) {

            Movie movie = getArguments().getParcelable(MainActivity.INTENT_EXTRA_MOVIE);

            if (movie != null) {

                TextView titleTextView = view.findViewById(R.id.overview_original_title);
                TextView ratingTextView = view.findViewById(R.id.overview_rating);
                TextView overviewTextView = view.findViewById(R.id.overview_content);

                titleTextView.setText(movie.getOriginalTitle());
                ratingTextView.setText(getRating(movie));
                overviewTextView.setText(movie.getOverview());

            }

        }

    }

    /**
     * get current selected movie rating
     *
     * @param movie selected movie
     * @return movie rating
     */
    private String getRating(Movie movie) {

        if (movie == null) {
            return "";
        } else {
            return movie.getVoteAverage() + "/10";
        }

    }

}
