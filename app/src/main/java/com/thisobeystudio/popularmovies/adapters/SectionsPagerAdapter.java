package com.thisobeystudio.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thisobeystudio.popularmovies.activities.OverviewFragment;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.activities.ReviewsFragment;
import com.thisobeystudio.popularmovies.activities.TrailersFragment;
import com.thisobeystudio.popularmovies.objects.Movie;

/**
 * Created by thisobeystudio on 11/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context = null;
    private Movie movie = null;

    public SectionsPagerAdapter(FragmentManager fm, Context context, Movie movie) {
        super(fm);
        this.context = context;
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position) {
            case 0:
                return OverviewFragment.newInstance(movie);
            case 1:
                return TrailersFragment.newInstance(movie);
            case 2:
                return ReviewsFragment.newInstance(movie);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.movie_overview_header);
            case 1:
                return context.getString(R.string.movie_trailers_header);
            case 2:
                return context.getString(R.string.movie_reviews_header);
        }
        return null;
    }

}