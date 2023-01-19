package com.sky.moviebonanza.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.sky.moviebonanza.R;
import com.sky.moviebonanza.adapters.MovieAdapter;
import com.sky.moviebonanza.databinding.FragmentMovieBinding;
import com.sky.moviebonanza.viewmodels.MovieViewmodel;

public class MovieSearchFragment  extends Fragment {

    private MovieAdapter adapter;
    private FragmentMovieBinding binding;
    private MovieViewmodel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MovieAdapter();

        viewModel =new ViewModelProvider(this).get(MovieViewmodel.class);
        viewModel.init();
        viewModel.getMovieResponseLiveData().observe(this, volumesResponse -> {
            if (volumesResponse != null) {
                binding.layoutMoviedata.setVisibility(View.VISIBLE);
                binding.layoutErrorData.setVisibility(View.GONE);
                adapter.setResults(volumesResponse);
            }else{
                binding.buttonMovieError.setEnabled(true);
                binding.layoutMoviedata.setVisibility(View.GONE);
                binding.layoutErrorData.setVisibility(View.VISIBLE);
            }
        });

        viewModel.loadMovies();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMovieBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        final int columns = getResources().getInteger(R.integer.gallery_columns);

        binding.recyclerviewMovie.setLayoutManager(new GridLayoutManager(getContext(),columns));
        binding.recyclerviewMovie.setAdapter(adapter);

        binding.search.setActivated(true);
        binding.search.setQueryHint(getResources().getString(R.string.search_hint));
        binding.search.onActionViewExpanded();
        binding.search.setIconified(false);
        binding.search.clearFocus();

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        binding.buttonMovieError.setOnClickListener(view1 -> {
            binding.buttonMovieError.setEnabled(false);
            viewModel.loadMovies();
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
