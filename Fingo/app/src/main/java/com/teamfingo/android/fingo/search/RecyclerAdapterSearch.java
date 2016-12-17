package com.teamfingo.android.fingo.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.common.ActivityMovieDetail;
import com.teamfingo.android.fingo.model.Movie;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 12. 6..
 */

public class RecyclerAdapterSearch extends RecyclerView.Adapter<RecyclerAdapterSearch.ViewHolder> {

    Context mContext;
    private ArrayList<Movie> mSearchMovies;
    private int itemLayout;


    public RecyclerAdapterSearch(Context context, ArrayList<Movie> searchMovies, int itemLayout) {
        this.mContext = context;
        this.mSearchMovies = searchMovies;
        this.itemLayout = itemLayout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout mRelativeLayout;
        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieDate;
        TextView tvMovieGenre;
        TextView tvMovieNation;

        public ViewHolder(View itemView) {
            super(itemView);

            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_search);
            ivMoviePoster = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.textView_movie_title);
            tvMovieDate = (TextView) itemView.findViewById(R.id.textView_movie_date);
            tvMovieGenre = (TextView) itemView.findViewById(R.id.textView_movie_genre);
            tvMovieNation = (TextView) itemView.findViewById(R.id.textView_movie_nation);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext).load(mSearchMovies.get(position).getImg()).into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(mSearchMovies.get(position).getTitle());
        holder.tvMovieDate.setText("(" + mSearchMovies.get(position).getFirst_run_date() + ")");
        Movie.Genre genre[] = mSearchMovies.get(position).getGenre();
        holder.tvMovieGenre.setText(genre[0].toString());
        Movie.Nation_code nationCode[] = mSearchMovies.get(position).getNation_code();
        holder.tvMovieNation.setText("/ " + nationCode[0]);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieId = mSearchMovies.get(position).getId();

                Intent intent = new Intent(mContext, ActivityMovieDetail.class);
                intent.putExtra("movieId", movieId);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchMovies.size();
    }
}
