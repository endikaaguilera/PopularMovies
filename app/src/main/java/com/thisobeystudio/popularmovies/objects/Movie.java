package com.thisobeystudio.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thisobeystudio on 29/7/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class Movie implements Parcelable {

    // AVAILABLE PARAMETERS >>> id, vote_count, video, vote_average, title, popularity, poster_path, original_language, original_title, genre_ids, backdrop_path, adult, overview, release_date;
    private final String id; // Can be int
    private final String voteAverage;
    private final String title;
    private final String posterPath;
    private final String originalLanguage;
    private final String originalTitle;
    private final String backdropPath;
    private final String overview;
    private final String releaseDate;

    public Movie(String id, String voteAverage, String title, String posterPath, String originalLanguage, String originalTitle, String backdropPath, String overview, String releaseDate) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.voteAverage = in.readString();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeInt(id);
        parcel.writeString(id);
        parcel.writeString(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    public String getId() {
        return id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
