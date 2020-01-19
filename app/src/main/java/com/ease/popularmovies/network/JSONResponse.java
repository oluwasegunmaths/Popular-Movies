package com.ease.popularmovies.network;

import com.ease.popularmovies.data.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JSONResponse {
    @SerializedName("results")

    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
