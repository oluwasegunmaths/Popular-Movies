package com.ease.popularmovies.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MovieApiService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    private static Retrofit retrofit;

    public static Call<JSONResponse> getCallToEnqueue(boolean sortingByPopularity) {
        buildRetrofit(null);
        MovieApiService.JsonPlaceHolder request = retrofit.create(MovieApiService.JsonPlaceHolder.class);
        Call<JSONResponse> call;
        if (sortingByPopularity) {
            call = request.getUserInfo();

        } else {
            call = request.getMoviesSortedByMostRated();

        }
        return call;
    }

    public static Call<ReviewJSONResponse> getReviewCallToEnqueue(String id) {
        buildRetrofit(id);
        Log.i("zzzzz", retrofit.baseUrl().toString());

        MovieApiService.JsonPlaceHolder request = retrofit.create(MovieApiService.JsonPlaceHolder.class);
        Call<ReviewJSONResponse> call;
        call = request.getReviews();
        return call;
    }

    public static Call<TrailerJSONResponse> getTrailerCallToEnqueue(String id) {
        buildRetrofit(id);
        MovieApiService.JsonPlaceHolder request = retrofit.create(MovieApiService.JsonPlaceHolder.class);
        Call<TrailerJSONResponse> call;
        call = request.getTrailers();
        return call;
    }

    private static void buildRetrofit(String id) {
        if (id == null) {
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
        } else {
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL + id + "/").build();

        }
    }
    public interface JsonPlaceHolder {
        @GET("popular?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<JSONResponse> getUserInfo();


        @GET("top_rated?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<JSONResponse> getMoviesSortedByMostRated();


        @GET("reviews?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<ReviewJSONResponse> getReviews();


        @GET("videos?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<TrailerJSONResponse> getTrailers();
    }
}
