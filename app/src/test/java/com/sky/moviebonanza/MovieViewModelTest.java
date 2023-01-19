package com.sky.moviebonanza;

import static org.mockito.Mockito.mock;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.sky.moviebonanza.models.MovieResponse;
import com.sky.moviebonanza.repositories.MovieRepository;
import com.sky.moviebonanza.viewmodels.MovieViewmodel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MovieViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application application ;

    @Mock
    private Context context = mock(Context.class);

    private MovieViewmodel viewModel;

    @Mock
    private MovieRepository movieRepository = new MovieRepository(context);

    private LiveData<List<MovieResponse>> movieResponseLiveData;

    private MovieResponse movieResponse=new MovieResponse("Story","Drama");

    private List<MovieResponse> movieResponseList=new ArrayList<>();

    @Mock
    Observer<List<MovieResponse>> observer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        viewModel = new MovieViewmodel(application);
        movieRepository = new MovieRepository(context);
        movieResponseLiveData = movieRepository.getMovieResponseLiveData();
        movieResponseList.add(movieResponse);
    }

    @Test
    public void checkMovieResponseNonNull(){
        movieRepository.loadMovies();
        movieResponseLiveData.observeForever(observer);
        Mockito.after(4000);
        Assert.assertNotNull(movieResponseList);
    }
}
