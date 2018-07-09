package com.thisobeystudio.popularmovies.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thisobeystudio.popularmovies.BuildConfig;
import com.thisobeystudio.popularmovies.R;
import com.thisobeystudio.popularmovies.adapters.TrailersRVAdapter;
import com.thisobeystudio.popularmovies.custom.GridSpacingItemDecoration;
import com.thisobeystudio.popularmovies.models.Movie;
import com.thisobeystudio.popularmovies.models.Trailer;
import com.thisobeystudio.popularmovies.utilities.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.thisobeystudio.popularmovies.utilities
        .PopularMoviesJSONUtils.getMoviesTrailersDataFromJSON;

/**
 * Created by thisobeystudio on 14/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class TrailersFragment extends Fragment {

    // tag used for debug logs
    private final String TAG = getClass().getSimpleName();

    // trailers_array args tag
    private final String TAG_TRAILERS_ARRAY = "trailers_array";

    // trailers data ArrayList
    private ArrayList<Trailer> trailersArray;

    public TrailersFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static TrailersFragment newInstance(@NonNull Movie movie) {
        TrailersFragment fragment = new TrailersFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.INTENT_EXTRA_MOVIE, movie);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (trailersArray != null) {
            outState.putParcelableArrayList(TAG_TRAILERS_ARRAY, trailersArray);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_trailers, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG_TRAILERS_ARRAY)) {

            trailersArray = savedInstanceState.getParcelableArrayList(TAG_TRAILERS_ARRAY);

            setupTrailersRecyclerView(view);

            Log.e(TAG, "trailersArray != null && contains key");

        } else if (trailersArray == null) {

            getTrailersData(view);

            Log.e(TAG, "trailersArray == null");

        } else {

            Log.e(TAG, "trailersArray != null");

            setupTrailersRecyclerView(view);

        }

    }

    /**
     * volley query to get current selected movie trailers JSON data
     *
     * @param v root view
     */
    private void getTrailersData(final View v) {

        // Not checking for internet, in case recycler view has saved trailers data
        if (getActivity() != null &&
                getArguments() != null &&
                getArguments().containsKey(MainActivity.INTENT_EXTRA_MOVIE)) {

            Movie movie = getArguments().getParcelable(MainActivity.INTENT_EXTRA_MOVIE);

            if (movie != null) {
                String movieId = movie.getId();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                //URL of the request we are sending
                String url = NetworkUtils.BASE_URL + "movie/" + movieId + "/" +
                        NetworkUtils.TRAILERS_QUERY + "?" +
                        NetworkUtils.API_KEY_QUERY + "=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;

                JsonObjectRequest sr = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                if (response != null) {

                                    trailersArray = getMoviesTrailersDataFromJSON(response);

                                    if (trailersArray != null) {

                                        setupTrailersRecyclerView(v);

                                    } else {
                                        Log.e(TAG, "NO AVAILABLE TRAILERS");
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
                    public Map<String, String> getHeaders() {
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
     * setup trailers RecyclerView
     *
     * @param v root view
     */
    private void setupTrailersRecyclerView(View v) {

        RecyclerView trailersRv = v.findViewById(R.id.trailers_rv);
        trailersRv.setVisibility(View.VISIBLE);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        trailersRv.setHasFixedSize(false);

        FragmentActivity activity = getActivity();

        if (activity == null) return;

        Resources res = activity.getResources();

        if (res == null) return;

        // use a linear manager
        int columns = res.getInteger(R.integer.youtube_items_per_row);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, columns);

        int margin = (int) res.getDimension(R.dimen.card_view_margin);
        trailersRv.addItemDecoration(new GridSpacingItemDecoration(columns, margin));

        trailersRv.setLayoutManager(mLayoutManager);

        trailersRv.setNestedScrollingEnabled(false); // Make scroll smoothly

        // specify an adapter
        TrailersRVAdapter trailersRVAdapter = new TrailersRVAdapter(activity, trailersArray);
        trailersRv.setAdapter(trailersRVAdapter);

    }

}