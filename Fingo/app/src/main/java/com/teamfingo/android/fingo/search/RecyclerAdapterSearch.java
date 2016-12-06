package com.teamfingo.android.fingo.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.SearchMovie;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 12. 6..
 */

public class RecyclerAdapterSearch extends RecyclerView.Adapter<RecyclerAdapterSearch.ViewHolder> {

    Context mContext;
    private ArrayList<SearchMovie> mSearchMovies;
    private int itemLayout;

    public RecyclerAdapterSearch(Context context, ArrayList<SearchMovie> mSearchMovies, int itemLayout) {
        this.mContext = context;
        this.mSearchMovies = mSearchMovies;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mSearchMovies.get(position).getImg()).into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mSearchMovies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public final ImageView ivMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);

        }
    }
}
