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

import com.thisobeystudio.popularmovies.GlideApp;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.adapters.SectionsPagerAdapter;
import com.thisobeystudio.popularmovies.data.MoviesProvider;
import com.thisobeystudio.popularmovies.models.Movie;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

import static com.thisobeystudio.popularmovies.data
        .PopularMoviesContract.PopularMoviesEntry.COLUMN_BACKDROP_PATH;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_ID;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_TITLE;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_OVERVIEW;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_POSTER_PATH;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_RELEASE_DATE;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_TITLE;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.COLUMN_VOTE_AVERAGE;
import static com.thisobeystudio.popularmovies.data.
        PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE) &&
                getArguments().containsKey(MovieDetailActivity.ARGS_URI)) {

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

        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getChildFragmentManager(), getActivity(), mMovie);

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

        if (v == null) return;

        // COLLAPSING TOOLBAR
        CollapsingToolbarLayout collapsingToolbarLayout =
                v.findViewById(R.id.collapsing_toolbar_layout);
        //collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#00FFFFFF"));
        collapsingToolbarLayout.setTitle(mMovie.getTitle());

        // BACKDROP IMAGE
        ImageView backdropImage = v.findViewById(R.id.backdrop_image);
        String backdropImageUrl = NetworkUtils.BACKDROP_IMAGES_URL + mMovie.getBackdropPath();

        GlideApp.with(v.getContext())
                .load(backdropImageUrl)
                .into(backdropImage);

        // POSTER IMAGE
        ImageView movieImage = v.findViewById(R.id.movie_image);
        movieImage.setScaleType(ImageView.ScaleType.FIT_XY);
        String posterImageUrl = NetworkUtils.IMAGES_URL + mMovie.getPosterPath();
        GlideApp.with(v.getContext())
                .load(posterImageUrl)
                .into(movieImage);

        // YEAR
        //String year = getString(R.string.movie_details_year) +
        // " " + mMovie.getReleaseDate().substring(0, 4);
        String year = mMovie.getReleaseDate();
        TextView yearTextView = v.findViewById(R.id.year_text_view);
        yearTextView.setText(year);

    }

    /**
     * setup fab button
     *
     * @param v root view
     */
    private void setupFabButton(View v) {

        fab = v.findViewById(R.id.fab);
        // CHECK IF IS A FAVORITE MOVIE AND HANDLE FAV BUTTON
        if (MoviesProvider.checkIfIsFavorite(getActivity(), mMovie.getId())) {

            //fab.setBackgroundTintList(
            // ContextCompat.getColorStateList(MovieDetailActivity.this, R.color.colorAccent));
            fab.setImageResource(R.drawable.ic_favoried_24dp);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view,
                            getString(R.string.removed_from_favorites),
                            Snackbar.LENGTH_SHORT)
                            .setAction("OK", null)
                            .show();
                    removeFromFavorites(mMovie.getId());

                }
            });

        } else {

            //fab.setBackgroundTintList(
            // ContextCompat.getColorStateList(MovieDetailActivity.this, R.color.colorAccent));
            fab.setImageResource(R.drawable.ic_unfavorite_24dp);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view,
                            getString(R.string.saved_to_favorites),
                            Snackbar.LENGTH_SHORT)
                            .setAction("OK", null)
                            .show();

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

                    Snackbar.make(view,
                            getString(R.string.removed_from_favorites),
                            Snackbar.LENGTH_SHORT)
                            .setAction("OK", null)
                            .show();

                    removeFromFavorites(mMovie.getId());
                }
            });

            fab.setImageResource(R.drawable.ic_favoried_24dp);

            ContentValues[] contentValuesArr = new ContentValues[1];

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID, mMovie.getId());
            contentValues.put(COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
            contentValues.put(COLUMN_TITLE, mMovie.getTitle());
            contentValues.put(COLUMN_POSTER_PATH, mMovie.getPosterPath());
            contentValues.put(COLUMN_ORIGINAL_LANGUAGE, mMovie.getOriginalLanguage());
            contentValues.put(COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());
            contentValues.put(COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
            contentValues.put(COLUMN_OVERVIEW, mMovie.getOverview());
            contentValues.put(COLUMN_RELEASE_DATE, mMovie.getReleaseDate());

            contentValuesArr[0] = contentValues;

            MainActivity mainActivity = ((MainActivity) getActivity());
            if (mainActivity == null) return;

            if (mainActivity.getMainUri() != null) {
                mUri = ((MainActivity) getActivity()).getMainUri();
            }

            if (mUri == null) return;

            if (mainActivity.getContentResolver()
                    .bulkInsert(CONTENT_URI_FAVORITES, contentValuesArr) > 0) {

                if (mUri.equals(CONTENT_URI_FAVORITES)) {
                    getActivity().getContentResolver().notifyChange(CONTENT_URI_FAVORITES, null);
                }

            }
        }
    }

    /**
     * removes movie from favorites by id
     * And update fab button OnClickListener
     *
     * @param id current selected movie id
     */
    private void removeFromFavorites(String id) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view,
                        getString(R.string.saved_to_favorites),
                        Snackbar.LENGTH_SHORT)
                        .setAction("OK", null)
                        .show();

                setAsFavorite();
            }
        });

        fab.setImageResource(R.drawable.ic_unfavorite_24dp);

        MainActivity mainActivity = ((MainActivity) getActivity());
        if (mainActivity == null) return;

        if (mainActivity.getMainUri() != null) {
            mUri = ((MainActivity) getActivity()).getMainUri();
        }

        if (mUri == null) return;

        Uri deleteUri = CONTENT_URI_FAVORITES.buildUpon().appendPath(String.valueOf(id)).build();
        if (mainActivity.getContentResolver().delete(deleteUri, null, null) > 0) {

            if (mUri.equals(CONTENT_URI_FAVORITES)) {
                getActivity().getContentResolver().notifyChange(CONTENT_URI_FAVORITES, null);
            }

        }

    }

}
