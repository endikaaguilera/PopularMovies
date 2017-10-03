package com.thisobeystudio.popularmovies.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.thisobeystudio.popularmovies.data.PopularMoviesContract;
import com.thisobeystudio.popularmovies.objects.Review;
import com.thisobeystudio.popularmovies.objects.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class PopularMoviesJSONUtils {

    /* Location information */
    private static final String PM_QUERY_RESULTS = "results";

    /* movies */
    // AVAILABLE PARAMETERS >>> id, vote_count, video, vote_average, title, popularity, poster_path, original_language, original_title, genre_ids, backdrop_path, adult, overview, release_date;
    private static final String PM_QUERY_ID = "id";
    private static final String PM_QUERY_VOTE_AVERAGE = "vote_average";
    private static final String PM_QUERY_TITLE = "title";
    private static final String PM_QUERY_POSTER_PATH = "poster_path";
    private static final String PM_QUERY_ORIGINAL_LANGUAGE = "original_language";
    private static final String PM_QUERY_ORIGINAL_TITLE = "original_title";
    private static final String PM_QUERY_BACKDROP_PATH = "backdrop_path";
    private static final String PM_QUERY_OVERVIEW = "overview";
    private static final String PM_QUERY_RELEASE_DATE = "release_date";

    /* reviews */
    // AVAILABLE PARAMETERS >>> "id", "author", "content" ,"url"
    private static final String PM_QUERY_AUTHOR = "author";
    private static final String PM_QUERY_CONTENT = "content";

    /* trailers */
    // AVAILABLE PARAMETERS >>> "id", "iso_639_1", "iso_3166_1", "key", "name", "site", "size", "type"
    private static final String PM_QUERY_KEY = "key";
    private static final String PM_QUERY_NAME = "name";
    private static final String PM_QUERY_SIZE = "size";

    /**
     * @param context context
     * @param jsonObject passed JSONObject
     * @param contentUri passed contentUri
     * @throws JSONException handle JSONException
     */
    public static void getPopularContentFromJson(Context context, JSONObject jsonObject, Uri contentUri) throws JSONException {

        if (jsonObject != null && jsonObject.has(PM_QUERY_RESULTS)) {

            JSONArray results = jsonObject.getJSONArray(PM_QUERY_RESULTS);

            ContentValues[] contentValuesArr = new ContentValues[results.length()];

            for (int i = 0; i < results.length(); i++) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ID, results.getJSONObject(i).getString(PM_QUERY_ID));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_VOTE_AVERAGE, results.getJSONObject(i).getString(PM_QUERY_VOTE_AVERAGE));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_TITLE, results.getJSONObject(i).getString(PM_QUERY_TITLE));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_POSTER_PATH, results.getJSONObject(i).getString(PM_QUERY_POSTER_PATH));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE, results.getJSONObject(i).getString(PM_QUERY_ORIGINAL_LANGUAGE));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_ORIGINAL_TITLE, results.getJSONObject(i).getString(PM_QUERY_ORIGINAL_TITLE));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_BACKDROP_PATH, results.getJSONObject(i).getString(PM_QUERY_BACKDROP_PATH));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_OVERVIEW, results.getJSONObject(i).getString(PM_QUERY_OVERVIEW));
                contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_RELEASE_DATE, results.getJSONObject(i).getString(PM_QUERY_RELEASE_DATE));

                contentValuesArr[i] = contentValues;

            }

            /* Get a handle on the ContentResolver to delete and insert data */
            ContentResolver contentResolver = context.getContentResolver();
            /* Delete old data */
            contentResolver.delete(
                    contentUri,
                    null,
                    null);

            /* Insert new data */
            contentResolver.bulkInsert(contentUri, contentValuesArr);

        }

    }

    /**
     * @param ob passed JSONObject
     * @return Trailers ArrayList
     */
    public static ArrayList<Trailer> getMoviesTrailersDataFromJSON(JSONObject ob) {

        ArrayList<Trailer> arrayList = new ArrayList<>();

        try {

            JSONArray results = ob.getJSONArray(PM_QUERY_RESULTS);

            for (int i = 0; i < results.length(); i++) {

                Trailer trailer = new Trailer(
                        results.getJSONObject(i).getString(PM_QUERY_ID),
                        results.getJSONObject(i).getString(PM_QUERY_KEY),
                        results.getJSONObject(i).getString(PM_QUERY_NAME),
                        results.getJSONObject(i).getString(PM_QUERY_SIZE));

                arrayList.add(trailer);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return arrayList;

    }

    /**
     * @param ob passed JSONObject
     * @return Reviews ArrayList
     */
    public static ArrayList<Review> getMoviesReviewsDataFromJSON(JSONObject ob) {

        ArrayList<Review> arrayList = new ArrayList<>();

        try {

            JSONArray results = ob.getJSONArray(PM_QUERY_RESULTS);

            for (int i = 0; i < results.length(); i++) {

                Review review = new Review(
                        results.getJSONObject(i).getString(PM_QUERY_ID),
                        results.getJSONObject(i).getString(PM_QUERY_AUTHOR),
                        results.getJSONObject(i).getString(PM_QUERY_CONTENT));

                arrayList.add(review);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return arrayList;

    }


}
