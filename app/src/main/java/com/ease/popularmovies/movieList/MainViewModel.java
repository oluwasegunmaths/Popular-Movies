package com.ease.popularmovies.movieList;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ease.popularmovies.AppExecutors;
import com.ease.popularmovies.R;
import com.ease.popularmovies.data.AppDatabase;
import com.ease.popularmovies.data.Movie;
import com.ease.popularmovies.network.JSONResponse;
import com.ease.popularmovies.network.MovieApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private final Context context;
    public boolean previousWasRatedSortBooleanSetupToPreventUserInterfaceConfusionWhenSwitchingBetweenMenuItems;

    public MutableLiveData<List<Movie>> movies = new MutableLiveData<List<Movie>>();
    public LiveData<List<Movie>> favMovies;

    public MutableLiveData<Boolean> messageTextIsVisible = new MutableLiveData<>();
    ;
    public MutableLiveData<Boolean> progressBartIsVisible = new MutableLiveData<>();
    ;
    public MutableLiveData<String> messageTextMessage = new MutableLiveData<>();
    ;
    public boolean isSortingByfavorites;


    public MainViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        AppDatabase mDb = AppDatabase.getInstance(application);
        favMovies = mDb.movieDao().loadFavoriteMovies();
        if (isThereConnection(context)) {
            loadMoviesOnBackgroundThread(true);
        } else {
            messageTextMessage.setValue(context.getText(R.string.no_network).toString());
            progressBartIsVisible.setValue(false);
        }

    }

    public static boolean isThereConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void loadMoviesOnBackgroundThread(final boolean sortingBypopularity) {


        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<JSONResponse> call = MovieApiService.getCallToEnqueue(sortingBypopularity);
                call.enqueue(new Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                        JSONResponse jsonResponse = response.body();
                        movies.setValue(jsonResponse.getMovies());
                        messageTextIsVisible.setValue(false);
                        progressBartIsVisible.setValue(false);


                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        messageTextIsVisible.setValue(true);
                        messageTextMessage.setValue(context.getText(R.string.error_loading_movies).toString());
                        progressBartIsVisible.setValue(false);


                    }
                });
            }
        });
    }
}
