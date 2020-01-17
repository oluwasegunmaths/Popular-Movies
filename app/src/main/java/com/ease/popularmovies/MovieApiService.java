package com.ease.popularmovies;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MovieApiService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    private static Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();

    public static Call<JSONResponse> getCallToEnqueue(boolean sortingByPopularity) {

        MovieApiService.JsonPlaceHolder request = retrofit.create(MovieApiService.JsonPlaceHolder.class);
        Call<JSONResponse> call;
        if (sortingByPopularity) {
            call = request.getUserInfo();

        } else {
            call = request.getMoviesSortedByMostRated();

        }
        return call;
    }


    public interface JsonPlaceHolder {
        @GET("popular?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<JSONResponse> getUserInfo();

        @GET("top_rated?api_key=[ENTER YOUR MOVIEDB API KEY HERE]")
        Call<JSONResponse> getMoviesSortedByMostRated();
    }
}
