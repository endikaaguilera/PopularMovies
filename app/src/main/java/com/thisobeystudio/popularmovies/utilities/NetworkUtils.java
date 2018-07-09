package com.thisobeystudio.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.thisobeystudio.popularmovies.BuildConfig.THE_MOVIE_DB_API_TOKEN;

/**
 * Created by thisobeystudio on 29/7/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public final class NetworkUtils {

    // url examples
    // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    // http://api.themoviedb.org/3/movie/324852/videos
    // https://www.youtube.com/watch?v=6DBi41reeF0

    // base url
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    // url paths
    public static final String SORT_BY_TOP_RATED = "movie/top_rated";
    public static final String SORT_BY_POPULAR = "movie/popular";
    public static final String SORT_BY_UPCOMING = "movie/upcoming";

    // url queries
    public static final String REVIEWS_QUERY = "reviews";
    public static final String TRAILERS_QUERY = "videos";

    // simple images base url
    public static final String IMAGES_URL = "http://image.tmdb.org/t/p/w185";

    // backdrop images base url
    public static final String BACKDROP_IMAGES_URL = "http://image.tmdb.org/t/p/w500";

    // youtube base url
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    // youtube thumbnails urls
    // https://img.youtube.com/vi/<insert-youtube-video-id-here>/0.jpg
    private static final String YOUTUBE_THUMBNAIL_URL_START = "https://img.youtube.com/vi/";
    private static final String YOUTUBE_THUMBNAIL_URL_END = "/0.jpg";

    // api key query param
    public static final String API_KEY_QUERY = "api_key";

    /**
     * @param context context
     * @return true if internet is available
     */
    public static boolean isInternetAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * @param sort sort type
     * @return url
     */
    public static String buildUrlString(String sort) {
        return BASE_URL + sort + "?" + API_KEY_QUERY + "=" + THE_MOVIE_DB_API_TOKEN;
    }

    /**
     * @param key youtube thumbnail key
     * @return youtube thumbnail url
     */
    public static String buildYoutubeThumbnailUrlString(String key) {
        return NetworkUtils.YOUTUBE_THUMBNAIL_URL_START +
                key +
                NetworkUtils.YOUTUBE_THUMBNAIL_URL_END;
    }

}

