package com.thisobeystudio.popularmovies.activities;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.adapters.SectionsPagerAdapter;
import com.thisobeystudio.popularmovies.data.MoviesProvider;
import com.thisobeystudio.popularmovies.data.PopularMoviesContract;
import com.thisobeystudio.popularmovies.objects.Movie;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

/**
 * Created by thisobeystudio on 14/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class MovieDetailsFragment extends Fragment {

    // tag used for debug logs
    private final String TAG = getClass().getSimpleName();

    // current selected Uri
    private Uri mUri;

    // selected movie
    private Movie mMovie;

    // fab button for favorite handling
    private FloatingActionButton fab;

    /**
     * Returns a new instance of this fragment
     */
    public static MovieDetailsFragment newInstance(@NonNull Movie movie, Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INTENT_EXTRA_MOVIE, movie);
        args.putString(MovieDetailActivity.ARGS_URI, "" + uri);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE) && getArguments().containsKey(MovieDetailActivity.ARGS_URI)) {

            mMovie = getArguments().getParcelable(MainActivity.INTENT_EXTRA_MOVIE);
            mUri = Uri.parse(getArguments().getString(MovieDetailActivity.ARGS_URI));

            if (mMovie != null) {

                loadToolBarContent(view);
                setupFabButton(view);
                setupPager(view);

            } else {
                Log.e(TAG, "Movie is null");
            }
        }

    }

    /**
     * setup ViewPager
     */
    private void setupPager(View v) {

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), getActivity(), mMovie);

        ViewPager mViewPager = v.findViewById(R.id.container);
        mViewPager.setAdapter(null);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(null);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * load ToolBar content
     */
    private void loadToolBarContent(View v) {

        // COLLAPSING TOOLBAR
        CollapsingToolbarLayout collapsingToolbarLayout = v.findViewById(R.id.collapsing_toolbar_layout);
        //collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#00FFFFFF"));
        collapsingToolbarLayout.setTitle(mMovie.getTitle());

        // BACKDROP IMAGE
        ImageView backdropImage = v.findViewById(R.id.backdrop_image);

        Glide.with(getActivity()).load(NetworkUtils.BACKDROP_IMAGES_URL + mMovie.getBackdropPath()).into(backdropImage);

        // POSTER IMAGE
        ImageView movieImage = v.findViewById(R.id.movie_image);
        movieImage.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getActivity()).load(NetworkUtils.IMAGES_URL + mMovie.getPosterPath()).into(movieImage);

        // YEAR
        //String year = getString(R.string.movie_details_year) + " " + mMovie.getReleaseDate().substring(0, 4);
        String year = mMovie.getReleaseDate();
        TextView yearTextView = v.findViewById(R.id.year_text_view);
        yearTextView.setText(year);

    }

    /**
     *
     * setup fab button
     *
     * @param v root view
     */
    private void setupFabButton(View v) {

        fab = v.findViewById(R.id.fab);
        // CHECK IF IS A FAVORITE MOVIE AND HANDLE FAV BUTTON
        if (MoviesProvider.checkIfIsFavorite(getActivity(), mMovie.getId())) {

            //fab.setBackgroundTintList(ContextCompat.getColorStateList(MovieDetailActivity.this, R.color.colorAccent));
            fab.setImageResource(R.drawable.ic_favoried_24dp);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_SHORT)
                            .setAction("OK", null).show();
                    removeFromFavorites(mMovie.getId());

                }
            });

        } else {

            //fab.setBackgroundTintList(ContextCompat.getColorStateList(MovieDetailActivity.this, R.color.colorAccent));
            fab.setImageResource(R.drawable.ic_unfavorite_24dp);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Saved to favorites", Snackbar.LENGTH_SHORT)
                            .setAction("OK", null).show();

                    setAsFavorite();

                }
            });
        }

    }

    /**
     * Add movie to Favorite db
     * And update fab button OnClickListener
     */
    private void setAsFavorite() {

        if (mMovie != null) {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_SHORT)
                            .setAction("OK", null).show();

                    removeFromFavorites(mMovie.getId());
                }
            });

            fab.setImageResource(R.drawable.ic_favoried_24dp);

            ContentValues[] contentValuesArr = new ContentValues[1];

            ContentValues contentValues = new ContentValues();
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ID, mMovie.getId());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_TITLE, mMovie.getTitle());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE, mMovie.getOriginalLanguage());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());

            contentValuesArr[0] = contentValues;

            if (((MainActivity) getActivity()).getMainUri() != null) {
                mUri = ((MainActivity) getActivity()).getMainUri();
            }

            if (getActivity().getContentResolver().bulkInsert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES, contentValuesArr) > 0) {

                if (mUri.equals(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES)) {
                    getActivity().getContentResolver().notifyChange(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES, null);
                }

            }

        }

    }

    /**
     *
     * removes movie from favorites by id
     * And update fab button OnClickListener
     *
     * @param id current selected movie id
     */
    private void removeFromFavorites(String id) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Saved to favorites", Snackbar.LENGTH_SHORT)
                        .setAction("OK", null).show();

                setAsFavorite();
            }
        });

        fab.setImageResource(R.drawable.ic_unfavorite_24dp);

        if (((MainActivity) getActivity()).getMainUri() != null) {
            mUri = ((MainActivity) getActivity()).getMainUri();
        }

        if (getActivity().getContentResolver().delete(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES.buildUpon().appendPath(String.valueOf(id)).build(), null, null) > 0) {

            if (mUri.equals(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES)) {
                getActivity().getContentResolver().notifyChange(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES, null);
            }

        }

    }

}
