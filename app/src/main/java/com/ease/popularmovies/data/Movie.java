package com.ease.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
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

    @PrimaryKey
    private int id;
    private int isFavoriteInt = 0;

    @Ignore
    public Movie() {
    }

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

    public Movie(int id, String posterUrl, String originalTitle, String plotSynopsisOverviewInApi, double userRatingVoteAverageInApi, String releaseDate) {
        this.id = id;
        this.posterUrl = posterUrl;
        this.originalTitle = originalTitle;
        this.plotSynopsisOverviewInApi = plotSynopsisOverviewInApi;
        this.userRatingVoteAverageInApi = userRatingVoteAverageInApi;
        this.releaseDate = releaseDate;
    }

    @Ignore

    protected Movie(Parcel in) {
        id = in.readInt();
        posterUrl = in.readString();
        originalTitle = in.readString();
        plotSynopsisOverviewInApi = in.readString();
        userRatingVoteAverageInApi = in.readDouble();
        releaseDate = in.readString();
        isFavoriteInt = in.readInt();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsFavoriteInt() {
        return isFavoriteInt;
    }

    public void setIsFavoriteInt(int isFavoriteInt) {
        this.isFavoriteInt = isFavoriteInt;
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
        dest.writeInt(id);
        dest.writeString(posterUrl);
        dest.writeString(originalTitle);
        dest.writeString(plotSynopsisOverviewInApi);
        dest.writeDouble(userRatingVoteAverageInApi);
        dest.writeString(releaseDate);
        dest.writeInt(isFavoriteInt);

    }
}
