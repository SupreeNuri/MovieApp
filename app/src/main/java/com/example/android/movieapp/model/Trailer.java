package com.example.android.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    private String id;
    private String youtubeLink;
    private String trailerName;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }
    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getTrailerName() {
        return trailerName;
    }
    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.youtubeLink);
        dest.writeString(this.trailerName);
    }

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.youtubeLink = in.readString();
        this.trailerName = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
