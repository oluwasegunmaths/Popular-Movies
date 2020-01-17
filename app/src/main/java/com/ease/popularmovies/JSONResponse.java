package com.ease.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONResponse {
    @SerializedName("results")

    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
