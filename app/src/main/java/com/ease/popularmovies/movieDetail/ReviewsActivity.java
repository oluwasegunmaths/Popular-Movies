package com.ease.popularmovies.movieDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.AppExecutors;
import com.ease.popularmovies.R;
import com.ease.popularmovies.network.MovieApiService;
import com.ease.popularmovies.network.ReviewJSONResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ease.popularmovies.movieList.MainViewModel.isThereConnection;

public class ReviewsActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "movie id";
    private TextView messageText;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initViews();
        setUpRecycler();
        String id = getIntent().getStringExtra(MOVIE_ID);
        setTitle(getString(R.string.reviews));
        if (id != null) {

            if (isThereConnection(this)) {
                loadReviews(id);
            } else {
                messageText.setVisibility(View.VISIBLE);
                messageText.setText(getText(R.string.no_network));
                progressBar.setVisibility(View.GONE);

            }
        }
    }

    private void loadReviews(final String id) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<ReviewJSONResponse> call = MovieApiService.getReviewCallToEnqueue(id);
                call.enqueue(new Callback<ReviewJSONResponse>() {
                    @Override
                    public void onResponse(Call<ReviewJSONResponse> call, Response<ReviewJSONResponse> response) {
                        ReviewJSONResponse jsonResponse = response.body();
                        Log.i("zzzzz", "");

                        adapter.setReviews(jsonResponse.getReviews());
                        progressBar.setVisibility(View.GONE);
                        messageText.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(Call<ReviewJSONResponse> call, Throwable t) {
                        Log.i("zzzzz", "fail");
                        messageText.setVisibility(View.VISIBLE);
                        messageText.setText(getText(R.string.error));
                        progressBar.setVisibility(View.GONE);


                    }
                });
            }
        });
    }

    private void setUpRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        adapter = new ReviewAdapter(this);
        recycler.setAdapter(adapter);
    }

    private void initViews() {
        messageText = findViewById(R.id.review_textview);
        recycler = findViewById(R.id.review_recycler);
        progressBar = findViewById(R.id.reviewprogressBar);
    }
}
