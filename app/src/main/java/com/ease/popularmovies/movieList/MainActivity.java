package com.ease.popularmovies.movieList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.R;
import com.ease.popularmovies.data.Movie;
import com.ease.popularmovies.movieDetail.MovieDetailActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {
    public static final String PARCEL_TO_INTENT = "parcel to intent";
    private TextView messageText;
    private RecyclerView recycler;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private Observer<List<Movie>> favObserver;
    private Observer<List<Movie>> downloadedMoviebserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        initViews();
        setUprecycler();
        downloadedMoviebserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        };
        favObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies == null || movies.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    messageText.setVisibility(View.VISIBLE);
                    messageText.setText(getText(R.string.you_have_no_fav_yet));
                    adapter.setMovies(null);

                } else {
                    messageText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    adapter.setMovies(movies);

                }

            }
        };


        if (viewModel.isSortingByfavorites) {
            viewModel.favMovies.observe(this, favObserver);

        } else {

            viewModel.movies.observe(this, downloadedMoviebserver);
        }

        viewModel.messageTextIsVisible.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    messageText.setVisibility(View.VISIBLE);
                } else {
                    messageText.setVisibility(View.GONE);

                }
            }
        });
        viewModel.progressBartIsVisible.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
        viewModel.messageTextMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                messageText.setText(message);
            }
        });
    }


    private void setUprecycler() {
        recycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
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
                if (viewModel.previousWasRatedSortBooleanSetupToPreventUserInterfaceConfusionWhenSwitchingBetweenMenuItems) {
                    viewModel.movies.setValue(null);
                    viewModel.messageTextIsVisible.setValue(true);
                    viewModel.progressBartIsVisible.setValue(true);
                    viewModel.messageTextMessage.setValue(getString(R.string.loading_movies));

                }
                viewModel.previousWasRatedSortBooleanSetupToPreventUserInterfaceConfusionWhenSwitchingBetweenMenuItems = false;
                if (viewModel.favMovies.hasObservers()) {
                    viewModel.favMovies.removeObservers(this);
                }

                viewModel.isSortingByfavorites = false;
                viewModel.loadMoviesOnBackgroundThread(true);

                viewModel.movies.observe(this, downloadedMoviebserver);

                return true;

            case R.id.action_rated_sort:
                if (!viewModel.previousWasRatedSortBooleanSetupToPreventUserInterfaceConfusionWhenSwitchingBetweenMenuItems) {
                    viewModel.movies.setValue(null);
                    viewModel.messageTextIsVisible.setValue(true);
                    viewModel.progressBartIsVisible.setValue(true);
                    viewModel.messageTextMessage.setValue(getString(R.string.loading_movies));
                }
                viewModel.previousWasRatedSortBooleanSetupToPreventUserInterfaceConfusionWhenSwitchingBetweenMenuItems = true;

                if (viewModel.favMovies.hasObservers()) {
                    viewModel.favMovies.removeObservers(this);

                }

                viewModel.isSortingByfavorites = false;
                viewModel.loadMoviesOnBackgroundThread(false);

                viewModel.movies.observe(this, downloadedMoviebserver);


                return true;

            case R.id.action_fav_sort:
                if (viewModel.movies.hasObservers()) {
                    viewModel.movies.removeObservers(this);
                }
                viewModel.isSortingByfavorites = true;


                viewModel.favMovies.observe(this, favObserver);

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
        if (viewModel.isSortingByfavorites) {
            intent.putExtra(PARCEL_TO_INTENT, viewModel.favMovies.getValue().get(position));

        } else {
            intent.putExtra(PARCEL_TO_INTENT, viewModel.movies.getValue().get(position));

        }
        startActivity(intent);


    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }
}
