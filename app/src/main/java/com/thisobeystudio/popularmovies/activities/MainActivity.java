package com.thisobeystudio.popularmovies.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.adapters.GridViewAdapter;
import com.thisobeystudio.popularmovies.data.MoviesProvider;
import com.thisobeystudio.popularmovies.data.PopularMoviesContract;
import com.thisobeystudio.popularmovies.models.Movie;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.thisobeystudio.popularmovies.utilities
        .PopularMoviesJSONUtils.getPopularContentFromJson;


/**
 * Popular Movies project for Udacity Android Developer Nanodegree Program
 * https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801
 * <p>
 * date: Summer 2017
 */

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // tag used for debug logs
    private final String TAG = MainActivity.class.getSimpleName();

    // sort code
    private int mSortCode = MoviesProvider.CODE_POPULAR;
    // sort Uri
    private Uri mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_POPULAR;
    // sort Url
    private String mSortUrl = NetworkUtils.buildUrlString(NetworkUtils.SORT_BY_POPULAR);
    // is sort by favorites
    private boolean mSortedByFavorites = false;

    // ProgressBar
    private ProgressBar progressBar;

    // movie extra tag
    public static final String INTENT_EXTRA_MOVIE = "movie";

    // GridView Adapter
    private GridViewAdapter gridViewAdapter;

    // The detail container view will be present only in the
    // large-screen layouts (res/values-sw600dp).
    // If this view is present, then the
    // activity should be in two-pane mode.
    private boolean mTwoPane = false;

    // query parameters index helpers
    //public static final int MAIN_TABLE_INDEX_ID = 0;
    private static final int MAIN_TABLE_INDEX_COLUMN_ID = 1;
    private final int MAIN_TABLE_INDEX_COLUMN_VOTE_AVERAGE = 2;
    private final int MAIN_TABLE_INDEX_COLUMN_TITLE = 3;
    public static final int MAIN_TABLE_INDEX_COLUMN_POSTER_PATH = 4;
    private final int MAIN_TABLE_INDEX_COLUMN_ORIGINAL_LANGUAGE = 5;
    private final int MAIN_TABLE_INDEX_COLUMN_ORIGINAL_TITLE = 6;
    private final int MAIN_TABLE_INDEX_COLUMN_BACKDROP_PATH = 7;
    private final int MAIN_TABLE_INDEX_COLUMN_OVERVIEW = 8;
    private final int MAIN_TABLE_INDEX_COLUMN_RELEASE_DATE = 9;

    // define query parameters
    private static final String[] MAIN_PROJECTION = {
            PopularMoviesContract.PopularMoviesEntry._ID,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_ID,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_VOTE_AVERAGE,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_TITLE,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_POSTER_PATH,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_TITLE,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_BACKDROP_PATH,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_OVERVIEW,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_RELEASE_DATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress_bar);
        // disable touch interactions on progress
        progressBar.setOnClickListener(null);

        // set sort values and init preferences if needed
        setSortTypeResources(getSortPreference());


        setProgressBarVisibility(View.VISIBLE);

        if (savedInstanceState == null) {
            loadData();
        } else {
            getLoaderManager().initLoader(mSortCode, savedInstanceState, this);
        }

        if (findViewById(R.id.details_fragment) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }

    }

    @Override
    protected void onDestroy() {
        // todo clear data here
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        showExitAppDialog();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_menu_popular_action:
                item.setChecked(!item.isChecked());
                setSortPreference(getString(R.string.pref_query_popular));
                break;

            case R.id.main_menu_top_rated_action:
                item.setChecked(!item.isChecked());
                setSortPreference(getString(R.string.pref_query_top_rated));
                break;

            case R.id.main_menu_upcoming_action:
                item.setChecked(!item.isChecked());
                setSortPreference(getString(R.string.pref_query_upcoming));
                break;

            case R.id.main_menu_favorites_action:
                item.setChecked(!item.isChecked());
                setSortPreference(getString(R.string.pref_query_favorite));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String sort = getSortPreference();
        switch (sort) {
            case "popular":
                menu.findItem(R.id.main_menu_popular_action).setChecked(true);
                break;
            case "top_rated":
                menu.findItem(R.id.main_menu_top_rated_action).setChecked(true);
                break;
            case "upcoming":
                menu.findItem(R.id.main_menu_upcoming_action).setChecked(true);
                break;
            case "favorite":
                menu.findItem(R.id.main_menu_favorites_action).setChecked(true);
                break;
            default:
                menu.findItem(R.id.main_menu_popular_action).setChecked(true);
                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        if (loaderId != mSortCode) {
            throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

        return new CursorLoader(this,
                mSortUri,
                MAIN_PROJECTION,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        setProgressBarVisibility(View.INVISIBLE);

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */

            if (gridViewAdapter != null) {
                gridViewAdapter.swapCursor(null);
            }

            updateErrorTextView(View.VISIBLE);

            return;
        }

        //Log.e(TAG, "data count =" + data.getCount());

        populateGridView(data);

    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid,
         * we need to clear the Adapter that is displaying the data.
         */
        gridViewAdapter.swapCursor(null);

    }

    /**
     * load activity data
     */
    private void loadData() {

        setProgressBarVisibility(View.VISIBLE);

        try {

            if (mSortedByFavorites) {
                getLoaderManager().restartLoader(mSortCode, null, this);
            } else if (NetworkUtils.isInternetAvailable(MainActivity.this)) {
                getMoviesJSON(mSortUrl, mSortUri);
            } else {
                getLoaderManager().initLoader(mSortCode, null, this);
            }

        } catch (Exception e) {

            e.printStackTrace();
            errorDialog(getString(R.string.error_dialog_query_error_title), getString(R.string.error_dialog_query_error_message));
            Log.e(TAG, "loadData try exception");

        }

    }

    /**
     * volley query to get Movies JSON data
     *
     * @param url        query url
     * @param contentUri content Uri
     */
    private void getMoviesJSON(String url, final Uri contentUri) {

        if (!url.equals("") && !mSortedByFavorites) {

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest sr = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (response != null) {

                                try {

                                    getPopularContentFromJson(
                                            MainActivity.this,
                                            response,
                                            contentUri);

                                    getLoaderManager().initLoader(mSortCode,
                                            null,
                                            MainActivity.this);

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    errorDialog(
                                            getString(R.string.error_dialog_query_error_title),
                                            getString(R.string.error_dialog_query_error_message));

                                    Log.e(TAG, "getMoviesJSON try response exception");

                                }

                            } else {

                                errorDialog(
                                        getString(R.string.error_dialog_query_error_title),
                                        getString(R.string.error_dialog_query_error_message));

                                Log.e(TAG, "response = null");

                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onResponse Error = " + error.getMessage());
                    errorDialog(getString(R.string.error_dialog_query_error_title), getString(R.string.error_dialog_query_error_message));
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //Map<String, String> params = new HashMap<String, String>();
                    //params.put("api_key", "MY_API_KEY");
                    return new HashMap<>();
                }

                @Override
                public Map<String, String> getHeaders() {
                    //Map<String, String> params = new HashMap<String, String>();
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };

            queue.add(sr);
        }

    }

    /**
     * populate GirdView, setAdapter and setOnItemClickListener
     *
     * @param data LoaderCallbacks data
     */
    private void populateGridView(final Cursor data) {

        gridViewAdapter = new GridViewAdapter(this, data);
        gridViewAdapter.swapCursor(data);

        setProgressBarVisibility(View.INVISIBLE);

        GridView gv = findViewById(R.id.main_grid_view);

        gv.setAdapter(gridViewAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mTwoPane) {

                    if (data != null) {

                        hideMovieChooserHint();

                        data.moveToPosition(position);

                        Movie movie = new Movie(data.getString(MAIN_TABLE_INDEX_COLUMN_ID),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_VOTE_AVERAGE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_TITLE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_POSTER_PATH),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_ORIGINAL_LANGUAGE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_ORIGINAL_TITLE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_BACKDROP_PATH),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_OVERVIEW),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_RELEASE_DATE));

                        MovieDetailsFragment fragment = MovieDetailsFragment
                                .newInstance(movie, mSortUri);

                        replaceDetailFragment(fragment);
                    }

                } else {

                    if (data != null) {

                        data.moveToPosition(position);

                        Movie movie = new Movie(data.getString(MAIN_TABLE_INDEX_COLUMN_ID),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_VOTE_AVERAGE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_TITLE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_POSTER_PATH),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_ORIGINAL_LANGUAGE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_ORIGINAL_TITLE),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_BACKDROP_PATH),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_OVERVIEW),
                                data.getString(MAIN_TABLE_INDEX_COLUMN_RELEASE_DATE));

                        Intent intent = new Intent(
                                MainActivity.this,
                                MovieDetailActivity.class);
                        intent.setData(mSortUri); // pass Uri
                        data.moveToPosition(position);
                        intent.putExtra(INTENT_EXTRA_MOVIE, movie);
                        startActivity(intent);
                    }
                }

            }
        });

        if (data == null) {
            errorDialog(getString(R.string.error_dialog_query_error_title), getString(R.string.error_dialog_query_error_message));
        }

    }

    /**
     * replace fragments
     *
     * @param fragment container
     */
    private void replaceDetailFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.details_fragment, fragment)
                .commit();
    }

    /**
     * get sort type form preferences
     *
     * @return current sort type
     */
    private String getSortPreference() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String key = getString(R.string.pref_sorting_key);

        String sort = sp.getString(key, "");

        if (sort.equals("")) {
            setSortPreference(getString(R.string.pref_query_popular));
            return getString(R.string.pref_query_popular);
        } else {
            return sort;
        }

    }

    /**
     * set new sort preference value
     *
     * @param sortType new sort type value
     */
    private void setSortPreference(String sortType) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(getString(R.string.pref_sorting_key), sortType);

        editor.apply();

        setSortTypeResources(sortType);
        loadData();

    }

    /**
     * update mSortCode, mSortUri, mSortUrl and mSortedByFavorites values based on sort type
     *
     * @param sort new sort type
     */
    private void setSortTypeResources(String sort) {

        if (getString(R.string.pref_query_popular).equals(sort)) {
            mSortCode = MoviesProvider.CODE_POPULAR;
            mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_POPULAR;
            mSortUrl = NetworkUtils.buildUrlString(NetworkUtils.SORT_BY_POPULAR);
            mSortedByFavorites = false;
        } else if (getString(R.string.pref_query_top_rated).equals(sort)) {
            mSortCode = MoviesProvider.CODE_TOP_RATED;
            mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_TOP_RATED;
            mSortUrl = NetworkUtils.buildUrlString(NetworkUtils.SORT_BY_TOP_RATED);
            mSortedByFavorites = false;
        } else if (getString(R.string.pref_query_upcoming).equals(sort)) {
            mSortCode = MoviesProvider.CODE_UPCOMING;
            mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_UPCOMING;
            mSortUrl = NetworkUtils.buildUrlString(NetworkUtils.SORT_BY_UPCOMING);
            mSortedByFavorites = false;
        } else if (getString(R.string.pref_query_favorite).equals(sort)) {
            mSortCode = MoviesProvider.CODE_FAVORITES;
            mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_FAVORITES;
            mSortUrl = "";
            mSortedByFavorites = true;
        } else {
            mSortCode = MoviesProvider.CODE_POPULAR;
            mSortUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI_POPULAR;
            mSortUrl = NetworkUtils.buildUrlString(NetworkUtils.SORT_BY_POPULAR);
            mSortedByFavorites = false;
        }

    }

    /**
     * update ProgressBar visibility
     *
     * @param visibility visibility state invisible/visible/gone
     */
    private void setProgressBarVisibility(int visibility) {

        progressBar.setVisibility(visibility);

        if (visibility == View.INVISIBLE) {
            updateErrorTextView(visibility);
        }

    }

    /**
     * update error TextView visibility
     *
     * @param visibility visibility state invisible/visible/gone
     */
    private void updateErrorTextView(int visibility) {

        TextView errorTextView = findViewById(R.id.main_error_text_view);
        errorTextView.setVisibility(visibility);

    }

    /**
     * Shows an error dialog
     *
     * @param title   error dialog title
     * @param message error dialog message
     */
    private void errorDialog(String title, String message) {

        updateErrorTextView(View.VISIBLE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), null);

        alertDialogBuilder.setNegativeButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                loadData();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    /**
     * Shows a dialog when back button is pressed that lets user quit the app
     */
    private void showExitAppDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.exit_app_dialog_title));
        alertDialogBuilder.setMessage(getString(R.string.exit_app_dialog_message));

        alertDialogBuilder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                MainActivity.super.onBackPressed();

            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.no), null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    /**
     * Hides Movie chooser hint TextView
     */
    private void hideMovieChooserHint() {

        TextView chooseMovieHint = findViewById(R.id.choose_movie_hint);

        if (chooseMovieHint != null) {
            chooseMovieHint.setVisibility(View.GONE);
        }

    }

    /**
     * @return current selected uri
     */
    public Uri getMainUri() {
        return mSortUri;
    }

}