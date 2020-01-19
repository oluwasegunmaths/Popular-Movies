package com.ease.popularmovies.network;

import com.ease.popularmovies.data.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerJSONResponse {
    @SerializedName("results")

    private List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
