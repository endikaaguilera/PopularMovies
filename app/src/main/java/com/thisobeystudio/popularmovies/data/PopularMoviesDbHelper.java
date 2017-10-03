package com.thisobeystudio.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thisobeystudio.popularmovies.data.PopularMoviesContract.PopularMoviesEntry;

/**
 * Created by thisobeystudio on 5/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

class PopularMoviesDbHelper extends SQLiteOpenHelper {

    // the DataBase name
    private static final String DATABASE_NAME = "movies.db";

    // the DataBase version
    private static final int DATABASE_VERSION = 1;

    PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_POPULAR_TABLE =
                "CREATE TABLE " + PopularMoviesEntry.TABLE_POPULAR + " (" +
                        PopularMoviesEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " + // UNIQUE so always from 1 to 20
                        PopularMoviesEntry.COLUMN_ID + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                        PopularMoviesEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_BACKDROP_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        //PopularMoviesEntry.COLUMN_FAVORITE + " REAL NOT NULL, " +
                        " UNIQUE (" + PopularMoviesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TOP_RATED_TABLE =
                "CREATE TABLE " + PopularMoviesEntry.TABLE_TOP_RATED + " (" +
                        PopularMoviesEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " + // UNIQUE so always from 1 to 20
                        PopularMoviesEntry.COLUMN_ID + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                        PopularMoviesEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_BACKDROP_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        //PopularMoviesEntry.COLUMN_FAVORITE + " REAL NOT NULL, " +
                        " UNIQUE (" + PopularMoviesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_UPCOMING_TABLE =
                "CREATE TABLE " + PopularMoviesEntry.TABLE_UPCOMING + " (" +
                        PopularMoviesEntry._ID + " INTEGER PRIMARY KEY UNIQUE, " + // UNIQUE so always from 1 to 20
                        PopularMoviesEntry.COLUMN_ID + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                        PopularMoviesEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_BACKDROP_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        //PopularMoviesEntry.COLUMN_FAVORITE + " REAL NOT NULL, " +
                        " UNIQUE (" + PopularMoviesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + PopularMoviesEntry.TABLE_FAVORITE + " (" +
                        PopularMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // AUTOINCREMENT
                        PopularMoviesEntry.COLUMN_ID + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                        PopularMoviesEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_POSTER_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_ORIGINAL_TITLE + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_BACKDROP_PATH + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        PopularMoviesEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        //PopularMoviesEntry.COLUMN_FAVORITE + " REAL NOT NULL, " +
                        " UNIQUE (" + PopularMoviesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_UPCOMING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    /**
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        AlterTables(sqLiteDatabase, oldVersion);

    }

    /**
     *
     * This is just an example of alter table on db upgrades. never will be executed due that the project is finished.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     */
    private void AlterTables(SQLiteDatabase sqLiteDatabase, int oldVersion) {

        // EXAMPLE OF ADDING TWO NEW COLUMNS TO FAVORITES TABLE ON DB UPGRADES

        final String NEW_COLUMN_1 = "new_column_1";
        final String DATABASE_ALTER_TABLE_FAVORITE_1 = "ALTER TABLE "
                + PopularMoviesEntry.TABLE_FAVORITE
                + " ADD COLUMN "
                + NEW_COLUMN_1
                + " string;";


        final String NEW_COLUMN_2 = "new_column_2";
        final String DATABASE_ALTER_TABLE_FAVORITE_2 = "ALTER TABLE "
                + PopularMoviesEntry.TABLE_FAVORITE
                + " ADD COLUMN "
                + NEW_COLUMN_2
                + " string;";

        // Every db upgrade add new if condition
        // ONE IN DB VERSION 2
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_TABLE_FAVORITE_1);
        }

        // Every db upgrade add new if condition
        // ANOTHER ONE ON DB VERSION 3
        if (oldVersion < 3) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_TABLE_FAVORITE_2);
        }

    }

    /*
     *
     *  This is just an example of drop tables
     *
     * @param sqLiteDatabase Database that is being upgraded
    */
    /*
    private void DropTables(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesEntry.TABLE_POPULAR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesEntry.TABLE_TOP_RATED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesEntry.TABLE_UPCOMING);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesEntry.TABLE_FAVORITE);

        onCreate(sqLiteDatabase);

    }
    */

}