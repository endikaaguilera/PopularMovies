package com.thisobeystudio.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class MoviesProvider extends ContentProvider {

    private PopularMoviesDbHelper mPopularOpenHelper;

    /*
     * These constant will be used to match URIs with the data they are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */
    public static final int CODE_POPULAR = 100;
    public static final int CODE_TOP_RATED = 200;
    public static final int CODE_UPCOMING = 300;
    public static final int CODE_FAVORITES = 400;
    private static final int CODE_FAVORITES_WITH_ID = 500;

    private final UriMatcher sUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMoviesContract.PATH_POPULAR, CODE_POPULAR);
        matcher.addURI(authority, PopularMoviesContract.PATH_TOP_RATED, CODE_TOP_RATED);
        matcher.addURI(authority, PopularMoviesContract.PATH_UPCOMING, CODE_UPCOMING);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        mPopularOpenHelper = new PopularMoviesDbHelper(getContext());

        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        switch (sUriMatcher.match(uri)) {

            case CODE_POPULAR:

                return myInsert(values, PopularMoviesContract.PopularMoviesEntry.TABLE_POPULAR, uri);

            case CODE_TOP_RATED:

                return myInsert(values, PopularMoviesContract.PopularMoviesEntry.TABLE_TOP_RATED, uri);

            case CODE_UPCOMING:

                return myInsert(values, PopularMoviesContract.PopularMoviesEntry.TABLE_UPCOMING, uri);

            case CODE_FAVORITES:

                return myInsert(values, PopularMoviesContract.PopularMoviesEntry.TABLE_FAVORITE, uri);

            default:
                return super.bulkInsert(uri, values);

        }

    }

    /**
     * @param values insert values
     * @param table target table
     * @param uri query Uri
     * @return rowsInserted
     */
    private int myInsert(ContentValues[] values, String table, Uri uri) {

        SQLiteDatabase db;

        db = mPopularOpenHelper.getWritableDatabase();
        db.beginTransaction();

        int rowsInserted = 0;

        try {
            for (ContentValues value : values) {

                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0 && !uri.toString().contains(PopularMoviesContract.PATH_FAVORITES)) {
            //if (rowsInserted > 0) {
            ContentResolver contentResolver = mGetContentResolver(getContext());
            if (contentResolver != null) {
                contentResolver.notifyChange(uri, null);
            }
        }

        return rowsInserted;

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_POPULAR: {

                cursor = mPopularOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_POPULAR,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_TOP_RATED: {

                cursor = mPopularOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_TOP_RATED,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_UPCOMING: {

                cursor = mPopularOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_UPCOMING,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITES: {

                cursor = mPopularOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                /*
                cursor = mPopularOpenHelper.getReadableDatabase().rawQuery(
                        "SELECT poster_path, favorite FROM popular WHERE favorite = 'YES' " +
                                "UNION " +
                                "SELECT poster_path, favorite FROM top_rated WHERE favorite = 'YES' " +
                                "UNION " +
                                "SELECT poster_path, favorite FROM upcoming WHERE favorite = 'YES'",
                        selectionArgs);

                cursor = "SELECT " +
                                "popular.poster_path, popular.favorite, " +
                                "top_rated.poster_path, top_rated.favorite, " +
                                "upcoming.poster_path, upcoming.favorite " +
                                //"FROM popular, upcoming WHERE popular.favorite = 'YES' OR upcoming.favorite = 'YES'", selectionArgs);
                                //"FROM popular JOIN upcoming ON popular.id = upcoming.id WHERE popular.favorite = 'YES' OR upcoming.favorite = 'YES'", null);
                                "FROM popular, top_rated, upcoming WHERE popular.favorite = 'YES' AND top_rated.favorite = 'YES' AND upcoming.favorite = 'YES'", null);
                */

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        ContentResolver contentResolver = mGetContentResolver(getContext());

        if (contentResolver != null) {

            cursor.setNotificationUri(contentResolver, uri);

        }

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_POPULAR:
                numRowsDeleted = mPopularOpenHelper.getWritableDatabase().delete(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_POPULAR,
                        selection,
                        selectionArgs);

                break;

            case CODE_TOP_RATED:
                numRowsDeleted = mPopularOpenHelper.getWritableDatabase().delete(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_TOP_RATED,
                        selection,
                        selectionArgs);

                break;

            case CODE_UPCOMING:
                numRowsDeleted = mPopularOpenHelper.getWritableDatabase().delete(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_UPCOMING,
                        selection,
                        selectionArgs);

                break;

            case CODE_FAVORITES_WITH_ID:
                numRowsDeleted = mPopularOpenHelper.getWritableDatabase().delete(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_FAVORITE,
                        PopularMoviesContract.PopularMoviesEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        /*
        if (numRowsDeleted != 0) {

            ContentResolver contentResolver = mGetContentResolver(getContext());

            if (contentResolver != null) {
                contentResolver.notifyChange(uri, null);
            }

        }
        */

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new RuntimeException(
                "Not implemented, using bulkInsert instead");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mPopularOpenHelper.close();
        super.shutdown();
    }

    /**
     * @param context context
     * @return contentResolver
     */
    private ContentResolver mGetContentResolver(Context context) {

        try {
            if (context == null) return null;
            else return context.getContentResolver();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param context context
     * @param id movie id
     * @return true if is in Favorite table
     */
    public static boolean checkIfIsFavorite(Context context, String id) {

        PopularMoviesDbHelper openHelper = new PopularMoviesDbHelper(context);

        SQLiteDatabase db;
        db = openHelper.getReadableDatabase();
        db.beginTransaction();

        boolean favorite;

        try {

            Cursor cursor = db.query(
                    PopularMoviesContract.PopularMoviesEntry.TABLE_FAVORITE,
                    null,
                    PopularMoviesContract.PopularMoviesEntry.COLUMN_ID + " = '" + id + "'",
                    null,
                    null,
                    null,
                    null);

            favorite = cursor.getCount() == 1;

            cursor.close();

            db.endTransaction();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();

            db.endTransaction();
            db.close();

            return false;
        }

        return favorite;

    }

}