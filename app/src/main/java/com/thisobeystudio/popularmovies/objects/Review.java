package com.thisobeystudio.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thisobeystudio on 4/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class Review implements Parcelable {

    // "id", "author", "content" ,"url"
    private final String id;
    private final String author;
    private final String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    private Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }

    /*
    public String getId() {
        return id;
    }
    */

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
