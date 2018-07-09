package com.thisobeystudio.popularmovies.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MovieDetailActivity extends AppCompatActivity {

    // tag used for debug logs
    //private final String TAG = getClass().getSimpleName();

    // uri args tag
    public static final String ARGS_URI = "uri";

    // current selected Uri
    private Uri mUri;

    // selected movie
    private Movie mMovie;

    // fab button for favorite handling
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null &&
                getIntent() != null &&
                getIntent().hasExtra(MainActivity.INTENT_EXTRA_MOVIE)) {

            mUri = getIntent().getData();
            mMovie = getIntent().getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE);

        } else if (savedInstanceState != null &&
                savedInstanceState.containsKey(ARGS_URI) &&
                savedInstanceState.containsKey(MainActivity.INTENT_EXTRA_MOVIE)) {

            mMovie = savedInstanceState.getParcelable(MainActivity.INTENT_EXTRA_MOVIE);

            mUri = Uri.parse(savedInstanceState.getString(ARGS_URI));

        } else {
            errorDialog(getString(R.string.error_dialog_query_error_title), getString(R.string.error_dialog_query_error_message));
        }

        if (mUri != null && mMovie != null) {

            setupPager();
            loadToolBarContent();
            setupFabButton();

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mUri != null && mMovie != null) {
            outState.putString(ARGS_URI, "" + mUri);
            outState.putParcelable(MainActivity.INTENT_EXTRA_MOVIE, mMovie);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Normally, calling setDisplayHomeAsUpEnabled(true) (we do so in onCreate here) as well as
         * declaring the parent activity in the AndroidManifest is all that is required to get the
         * up button working properly. However, in this case, we want to navigate to the previous
         * screen the user came from when the up button was clicked, rather than a single
         * designated Activity in the Manifest.
         *
         * We use the up button's ID (android.R.id.home) to listen for when the up button is
         * clicked and then call onBackPressed to navigate to the previous Activity when this
         * happens.
         */
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * setup ViewPager
     */
    private void setupPager() {

        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager(), this, mMovie);

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * load ToolBar content
     */
    private void loadToolBarContent() {

        // COLLAPSING TOOLBAR
        CollapsingToolbarLayout ctl = findViewById(R.id.collapsing_toolbar_layout);
        //collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#00FFFFFF"));
        ctl.setTitle(mMovie.getTitle());

        // BACKDROP IMAGE
        ImageView backdropImage = findViewById(R.id.backdrop_image);
        String backdropImageUrl = NetworkUtils.BACKDROP_IMAGES_URL + mMovie.getBackdropPath();
        GlideApp.with(MovieDetailActivity.this)
                .load(backdropImageUrl)
                .into(backdropImage);

        // POSTER IMAGE
        ImageView movieImage = findViewById(R.id.movie_image);
        movieImage.setScaleType(ImageView.ScaleType.FIT_XY);
        String posterImageUrl = NetworkUtils.IMAGES_URL + mMovie.getPosterPath();
        GlideApp.with(MovieDetailActivity.this)
                .load(posterImageUrl)
                .into(movieImage);

        // YEAR
        String year = mMovie.getReleaseDate();
        TextView yearTextView = findViewById(R.id.year_text_view);
        yearTextView.setText(year);

    }

    /**
     * setup fab button
     */
    private void setupFabButton() {

        fab = findViewById(R.id.fab);
        // CHECK IF IS A FAVORITE MOVIE AND HANDLE FAV BUTTON
        if (MoviesProvider.checkIfIsFavorite(MovieDetailActivity.this, mMovie.getId())) {

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

            if (getContentResolver().bulkInsert(CONTENT_URI_FAVORITES, contentValuesArr) > 0) {

                if (mUri.equals(CONTENT_URI_FAVORITES)) {
                    getContentResolver().notifyChange(CONTENT_URI_FAVORITES, null);
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


        Uri deleteUri = CONTENT_URI_FAVORITES.buildUpon().appendPath(String.valueOf(id)).build();

        if (getContentResolver().delete(deleteUri, null, null) > 0) {

            if (mUri.equals(CONTENT_URI_FAVORITES)) {
                getContentResolver().notifyChange(CONTENT_URI_FAVORITES, null);
            }

        }

    }

    /**
     * Error AlertDialog
     *
     * @param title   dialog title
     * @param message dialog content msg
     */
    private void errorDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), null);

        alertDialogBuilder.setNegativeButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                recreate();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

}
