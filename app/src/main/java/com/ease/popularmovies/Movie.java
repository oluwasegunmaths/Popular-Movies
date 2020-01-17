package com.ease.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("poster_path")

    private String posterUrl;
    @SerializedName("original_title")

    private String originalTitle;
    @SerializedName("overview")

    private String plotSynopsisOverviewInApi;
    @SerializedName("vote_average")

    private double userRatingVoteAverageInApi;
    @SerializedName("release_date")

    private String releaseDate;

    public Movie() {
    }

    public Movie(String posterUrl, String originalTitle, String plotSynopsisOverviewInApi, double userRatingVoteAverageInApi, String releaseDate) {
        this.posterUrl = posterUrl;
        this.originalTitle = originalTitle;
        this.plotSynopsisOverviewInApi = plotSynopsisOverviewInApi;
        this.userRatingVoteAverageInApi = userRatingVoteAverageInApi;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        posterUrl = in.readString();
        originalTitle = in.readString();
        plotSynopsisOverviewInApi = in.readString();
        userRatingVoteAverageInApi = in.readDouble();
        releaseDate = in.readString();
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlotSynopsisOverviewInApi() {
        return plotSynopsisOverviewInApi;
    }

    public void setPlotSynopsisOverviewInApi(String plotSynopsisOverviewInApi) {
        this.plotSynopsisOverviewInApi = plotSynopsisOverviewInApi;
    }

    public double getUserRatingVoteAverageInApi() {
        return userRatingVoteAverageInApi;
    }

    public void setUserRatingVoteAverageInApi(double userRatingVoteAverageInApi) {
        this.userRatingVoteAverageInApi = userRatingVoteAverageInApi;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsisOverviewInApi);
        dest.writeDouble(userRatingVoteAverageInApi);
        dest.writeString(releaseDate);
    }
}
