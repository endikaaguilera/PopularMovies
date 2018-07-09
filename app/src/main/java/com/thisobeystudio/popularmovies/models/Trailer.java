package com.thisobeystudio.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thisobeystudio on 4/8/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

public class Trailer implements Parcelable {

    // "id", "iso_639_1", "iso_3166_1", "key", "name", "site", "size", "type"
    private final String id;
    private final String key;
    private final String name;
    private final String size;

    public Trailer(String id, String key, String name, String size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
    }

    private Trailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.size = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(size);
    }

    /*
    public String getId() {
        return id;
    }
    */

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

}
