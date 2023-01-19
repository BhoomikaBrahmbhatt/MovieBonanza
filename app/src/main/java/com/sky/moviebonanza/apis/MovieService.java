package com.sky.moviebonanza.apis;

import com.sky.moviebonanza.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("/b/YNFW")
    Call<List<MovieResponse>> loadMovies();
}
