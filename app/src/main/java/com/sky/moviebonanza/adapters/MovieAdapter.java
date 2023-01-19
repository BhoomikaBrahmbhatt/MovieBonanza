package com.sky.moviebonanza.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sky.moviebonanza.R;
import com.sky.moviebonanza.databinding.MovieItemBinding;
import com.sky.moviebonanza.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieResultHolder> implements Filterable {
    private List<MovieResponse> mList = new ArrayList<>();
    private List<MovieResponse> mFilterList= new ArrayList<>();
    ValueFilter valueFilter;
    @NonNull
    @Override
    public MovieResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding binding = MovieItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MovieResultHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieResultHolder holder, int position) {
        MovieResponse volume = mFilterList.get(position);

        holder.binding.movieItemGenre.setText(volume.getGenre());

        if (volume.getPoster() != null) {
            String imageUrl = volume.getPoster()
                    .replace("http://", "https://");

            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .error(R.drawable.movie_not)
                    .into(holder.binding.movieItemSmallThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public void setResults(List<MovieResponse> results) {
        this.mFilterList = results;
        this.mList = results;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<MovieResponse> filterList = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    if ((mList.get(i).getTitle().toUpperCase()).contains(constraint.toString().toUpperCase()) ||
                            (mList.get(i).getGenre().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mList.size();
                results.values = mList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
                mFilterList = (List<MovieResponse>) results.values;
                notifyDataSetChanged();
        }
    }

    protected static class MovieResultHolder extends RecyclerView.ViewHolder{

        MovieItemBinding binding;

        public MovieResultHolder(MovieItemBinding b){
            super(b.getRoot());
            binding = b;
        }
    }
}
