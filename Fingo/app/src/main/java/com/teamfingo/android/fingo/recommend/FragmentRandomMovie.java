package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.MovieWrapper;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRandomMovie extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;
    RecyclerAdapterRandomMovie mRecyclerAdapterRandomMovie;
    ArrayList<Movie> mRandomMovies = new ArrayList<>();

    public static FragmentRandomMovie newInstance() {
        FragmentRandomMovie f = new FragmentRandomMovie();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_movie, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_random_movie);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_random_movie);
        mRecyclerAdapterRandomMovie = new RecyclerAdapterRandomMovie(getContext(), mRandomMovies, R.layout.item_random_movie);
        mRecyclerView.setAdapter(mRecyclerAdapterRandomMovie);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData();

        // swipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void loadData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mRandomMovies.clear();

        Call<MovieWrapper> getRandomMovieCall = AppController.getFingoService()
                .getRandomMovie(AppController.getToken());
        getRandomMovieCall.enqueue(new Callback<MovieWrapper>() {
            @Override
            public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                if (response.isSuccessful()) {
                    MovieWrapper randomMovie = response.body();

                    mRandomMovies.addAll(randomMovie.getData());
                    mRecyclerAdapterRandomMovie.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MovieWrapper> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
