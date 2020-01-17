package com.ease.popularmovies;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static com.ease.popularmovies.MovieAdapter.BASE_IMAGE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Movie movie = getIntent().getParcelableExtra(MainActivity.PARCEL_TO_INTENT);
        if (movie != null) {
            populateUi(movie);
            setTitle(movie.getOriginalTitle());
        }
    }

    private void populateUi(Movie movie) {
        ImageView posterView = findViewById(R.id.poster_image_view);
        loadImageUsingPicassoAccordingToBuildVersion(movie, posterView);

        TextView titleTextView = findViewById(R.id.title_tv);
        TextView releaseDateTextView = findViewById(R.id.release_date_tv);
        TextView voteAverageTextView = findViewById(R.id.vote_average_tv);
        TextView plotSynopsisTextView = findViewById(R.id.plot_synopsis);


        titleTextView.setText(movie.getOriginalTitle());
        releaseDateTextView.setText(movie.getReleaseDate());
        voteAverageTextView.setText(String.valueOf(movie.getUserRatingVoteAverageInApi()));
        plotSynopsisTextView.setText(movie.getPlotSynopsisOverviewInApi());

    }

    private void loadImageUsingPicassoAccordingToBuildVersion(Movie movie, ImageView posterView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable errorDrawable = getDrawable(R.drawable.ic_action_error);
            if (errorDrawable != null) {
                Picasso.get().load(BASE_IMAGE_URL + movie.getPosterUrl()).error(errorDrawable).into(posterView);
            } else {
                Picasso.get().load(BASE_IMAGE_URL + movie.getPosterUrl()).into(posterView);

            }
        } else {
            Picasso.get().load(BASE_IMAGE_URL + movie.getPosterUrl()).into(posterView);

        }

        Picasso.get().load(BASE_IMAGE_URL + movie.getPosterUrl()).into(posterView);
    }
}
