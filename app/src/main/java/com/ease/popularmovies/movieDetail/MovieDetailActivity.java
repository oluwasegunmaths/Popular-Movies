package com.ease.popularmovies.movieDetail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.AppExecutors;
import com.ease.popularmovies.R;
import com.ease.popularmovies.data.AppDatabase;
import com.ease.popularmovies.data.Movie;
import com.ease.popularmovies.movieList.MainActivity;
import com.ease.popularmovies.network.MovieApiService;
import com.ease.popularmovies.network.TrailerJSONResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ease.popularmovies.movieList.MovieAdapter.BASE_IMAGE_URL;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener {

    private boolean isFavorite;
    private MenuItem item;
    private AppDatabase mDb;
    private Movie movie;
    private int movieId;
    private Movie movieFromDatabase;
    private RecyclerView recycler;
    private TrailerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initDatabase();


        movie = getIntent().getParcelableExtra(MainActivity.PARCEL_TO_INTENT);
        if (movie != null) {
            movieId = movie.getId();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    movieFromDatabase = mDb.movieDao().loadMovieById(movieId);
                    if (movieFromDatabase != null) {
                        isFavorite = true;
                        invalidateOptionsMenu();

                    }
                }
            });
            populateUi(movie);

            setTitle(movie.getOriginalTitle());
            setUpRecycler();
            loadTrailers(String.valueOf(movieId));
        }

    }

    private void initDatabase() {
        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    private void loadTrailers(final String id) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<TrailerJSONResponse> call = MovieApiService.getTrailerCallToEnqueue(id);
                call.enqueue(new Callback<TrailerJSONResponse>() {
                    @Override
                    public void onResponse(Call<TrailerJSONResponse> call, Response<TrailerJSONResponse> response) {
                        TrailerJSONResponse jsonResponse = response.body();
                        adapter.setTrailerList(jsonResponse.getTrailers());


                    }

                    @Override
                    public void onFailure(Call<TrailerJSONResponse> call, Throwable t) {


                    }
                });
            }
        });
    }

    private void setUpRecycler() {
        recycler = findViewById(R.id.trailer_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        adapter = new TrailerAdapter(this, this);
        recycler.setAdapter(adapter);
    }

    private void populateUi(Movie movie) {
        ImageView posterView = findViewById(R.id.poster_image_view);
        loadImageUsingPicassoAccordingToBuildVersion(movie, posterView);

        TextView releaseDateTextView = findViewById(R.id.release_date_tv);
        TextView voteAverageTextView = findViewById(R.id.vote_average_tv);
        TextView plotSynopsisTextView = findViewById(R.id.plot_synopsis);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        item = menu.findItem(R.id.action_favorite);
        if (isFavorite) {
            item.setIcon(R.drawable.ic_favorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (!isFavorite) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movie.setIsFavoriteInt(1);
                            mDb.movieDao().insertMovie(movie);
                            isFavorite = true;

                        }
                    });
                    item.setIcon(R.drawable.ic_favorite);

                    Toast.makeText(this, movie.getOriginalTitle() + " ADDED TO FAVORITES ", Toast.LENGTH_SHORT).show();

                } else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movie.setIsFavoriteInt(0);

                            mDb.movieDao().deleteMovieById(movie.getId());
                            isFavorite = false;

                        }
                    });
                    item.setIcon(R.drawable.ic_not_favorite);

                    Toast.makeText(this, movie.getOriginalTitle() + " REMOVED FROM FAVORITES ", Toast.LENGTH_SHORT).show();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToReviews(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(ReviewsActivity.MOVIE_ID, String.valueOf(movieId));


        startActivity(intent);
    }

    @Override
    public void onItemClick(String key) {
        String url = "https://www.youtube.com/watch?v=" + key;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onShareImageClick(String url) {
        String intentContent = "Check out this awesome " + movie.getOriginalTitle() + " trailer @: https://www.youtube.com/watch?v=" + url;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_TEXT, intentContent);
        startActivity(Intent.createChooser(shareIntent, "Select APP to Share WIth"));
    }
}
