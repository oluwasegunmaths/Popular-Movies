package com.ease.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {
    public static final String PARCEL_TO_INTENT = "parcel to intent";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private TextView messageText;
    private RecyclerView recycler;
    private MovieAdapter adapter;
    private List<Movie> movies;
    private ProgressBar progressBar;

    public static boolean isThereConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setUprecycler();


        if (isThereConnection(this)) {
            loadMoviesAndSetToAdapterOnBackgroundThread(true);
        } else {
            messageText.setText(getText(R.string.no_network));
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadMoviesAndSetToAdapterOnBackgroundThread(final boolean sortingBypopularity) {


        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<JSONResponse> call = MovieApiService.getCallToEnqueue(sortingBypopularity);
                call.enqueue(new Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                        JSONResponse jsonResponse = response.body();
                        movies = jsonResponse.getMovies();
                        adapter.setMovies(movies);
                        messageText.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        messageText.setText(getText(R.string.error_loading_movies));
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    private void setUprecycler() {
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter(this, this);
        recycler.setAdapter(adapter);
    }

    private void initViews() {
        messageText = findViewById(R.id.main_textview);
        recycler = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pop_sort:
                loadMoviesAndSetToAdapterOnBackgroundThread(true);
                return true;

            case R.id.action_rated_sort:
                loadMoviesAndSetToAdapterOnBackgroundThread(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(PARCEL_TO_INTENT, movies.get(position));
        startActivity(intent);


    }
}
