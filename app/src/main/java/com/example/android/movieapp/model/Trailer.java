package com.example.android.movieapp.model;

public class Trailer {
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
}
