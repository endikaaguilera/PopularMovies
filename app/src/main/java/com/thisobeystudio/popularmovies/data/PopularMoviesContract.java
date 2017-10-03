package com.thisobeystudio.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

final public class PopularMoviesContract {

    static final String CONTENT_AUTHORITY = "com.thisobeystudio.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_POPULAR = "popular";           // popular table path
    static final String PATH_TOP_RATED = "top_rated";       // top rated table path
    static final String PATH_UPCOMING = "upcoming";         // upcoming table path
    static final String PATH_FAVORITES = "favorites";       // favorites table path

    /* Inner class that defines the table contents of the table */
    public static final class PopularMoviesEntry implements BaseColumns {

        /* The base CONTENT_URIS used to query the table from the content provider */

        public static final Uri CONTENT_URI_POPULAR = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR)
                .build();

        public static final Uri CONTENT_URI_TOP_RATED = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();

        public static final Uri CONTENT_URI_UPCOMING = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_UPCOMING)
                .build();

        public static final Uri CONTENT_URI_FAVORITES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        /* Used internally as the name of our tables. */
        static final String TABLE_POPULAR = "popular";
        static final String TABLE_TOP_RATED = "top_rated";
        static final String TABLE_UPCOMING = "upcoming";
        static final String TABLE_FAVORITE = "favorite";

        /* Used internally as the name of our columns. */
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }
}
