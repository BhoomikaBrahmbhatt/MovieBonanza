package com.sky.moviebonanza.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sky.moviebonanza.models.MovieResponse;
import com.sky.moviebonanza.repositories.MovieRepository;

import java.util.List;

public class MovieViewmodel  extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<MovieResponse>> movieResponseLiveData;

    public MovieViewmodel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        movieRepository = new MovieRepository(getApplication().getApplicationContext());
        movieResponseLiveData = movieRepository.getMovieResponseLiveData();
    }

    public void loadMovies() {
        movieRepository.loadMovies();
    }

    public LiveData<List<MovieResponse>> getMovieResponseLiveData() {
        return movieResponseLiveData;
    }
}
