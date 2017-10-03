package com.thisobeystudio.popularmovies.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thisobeystudio.popularmovies.BuildConfig;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.adapters.ReviewsRVAdapter;
import com.thisobeystudio.popularmovies.objects.Movie;
import com.thisobeystudio.popularmovies.objects.Review;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;
import com.thisobeystudio.popularmovies.utilities.PopularMoviesJSONUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thisobeystudio on 14/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class ReviewsFragment extends Fragment {

    // tag used for debug logs
    private final String TAG = getClass().getSimpleName();

    // reviews_array args tag
    private final String TAG_REVIEWS_ARRAY = "reviews_array";

    // reviews data ArrayList
    private ArrayList<Review> reviewsArray;

    public ReviewsFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static ReviewsFragment newInstance(@NonNull Movie movie) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INTENT_EXTRA_MOVIE, movie);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (reviewsArray != null) {
            outState.putParcelableArrayList(TAG_REVIEWS_ARRAY, reviewsArray);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_reviews, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG_REVIEWS_ARRAY)) {

            reviewsArray = savedInstanceState.getParcelableArrayList(TAG_REVIEWS_ARRAY);

            setupReviewsRecyclerView(view);

        } else if (reviewsArray == null) {

            getReviewsData(view);

        } else {

            setupReviewsRecyclerView(view);

        }

    }

    /**
     *
     * volley query to get current selected movie reviews JSON data
     *
     * @param view root view
     */
    private void getReviewsData(final View view) {

        // Not checking for internet, in case recycler view saved reviews data
        //if (getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE) && NetworkUtils.isInternetAvailable(getActivity())) {
        if (getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE)) {

            Movie movie = getArguments().getParcelable(MainActivity.INTENT_EXTRA_MOVIE);

            if (movie != null) {

                String movieId = movie.getId();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                //URL of the request we are sending
                String url = NetworkUtils.BASE_URL + "movie/" + movieId + "/" + NetworkUtils.REVIEWS_QUERY + "?" + NetworkUtils.API_KEY_QUERY + "=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;

                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Log.e("MovieDetails", "Response = " + response);

                        if (response != null) {

                            reviewsArray = PopularMoviesJSONUtils.getMoviesReviewsDataFromJSON(response);

                            if (reviewsArray != null) {

                                setupReviewsRecyclerView(view);

                            } else {
                                Log.e(TAG, "NO AVAILABLE REVIEWS");
                            }

                        } else {
                            Log.e(TAG, "RESPONSE = NULL");
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "onResponse Error = " + error.getMessage());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //Map<String, String> params = new HashMap<String, String>();
                        //params.put(NetworkUtils.API_KEY_QUERY, BuildConfig.THE_MOVIE_DB_API_TOKEN);
                        return new HashMap<>();
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };

                queue.add(sr);
            }
        }
    }

    /**
     *
     * setup reviews RecyclerView
     *
     * @param v root view
     */
    private void setupReviewsRecyclerView(View v) {

        RecyclerView reviewsRv = v.findViewById(R.id.reviews_rv);

        reviewsRv.setVisibility(View.VISIBLE);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        reviewsRv.setHasFixedSize(false);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        reviewsRv.setLayoutManager(mLayoutManager);

        reviewsRv.setNestedScrollingEnabled(false); // Make scroll smoothly

        ReviewsRVAdapter reviewsRVAdapter = new ReviewsRVAdapter(reviewsArray);
        reviewsRv.setAdapter(reviewsRVAdapter);

    }

}
