package com.ease.popularmovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie ")
    LiveData<List<Movie>> loadFavoriteMovies();

    @Query("SELECT * FROM Movie WHERE  id=:id")
    Movie loadMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(Movie movie);

    @Query("DELETE FROM Movie WHERE id =:id")
    void deleteMovieById(int id);


}
