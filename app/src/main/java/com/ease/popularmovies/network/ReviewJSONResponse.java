package com.ease.popularmovies.network;

import com.ease.popularmovies.data.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewJSONResponse {
    @SerializedName("results")

    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }
}