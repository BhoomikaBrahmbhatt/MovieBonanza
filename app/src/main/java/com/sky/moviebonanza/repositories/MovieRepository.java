package com.sky.moviebonanza.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sky.moviebonanza.apis.MovieService;
import com.sky.moviebonanza.models.MovieResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String MOVIE_SERVICE_BASE_URL = "https://www.jsonkeeper.com/";

    private final MovieService movieService;
    private final MutableLiveData<List<MovieResponse>> movieResponseLiveData;

    public MovieRepository(Context applicationContext) {
        movieResponseLiveData = new MutableLiveData<>();

        File httpCacheDirectory = new File(applicationContext.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache)
                .build();

        movieService = new retrofit2.Retrofit.Builder()
                .baseUrl(MOVIE_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);

    }
    public static class CacheInterceptor implements Interceptor {
        @NonNull
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(10, TimeUnit.MINUTES) // 10 minutes cache
                    .build();

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build();
        }
    }
    public void loadMovies() {
        movieService.loadMovies().enqueue(new Callback<List<MovieResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovieResponse>> call, @NonNull Response<List<MovieResponse>> response) {
                if (response.body() != null) {
                    movieResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MovieResponse>> call, @NonNull Throwable t) {
                movieResponseLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<MovieResponse>> getMovieResponseLiveData() {
        return movieResponseLiveData;
    }
}
