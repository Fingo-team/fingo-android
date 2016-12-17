package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.RandomMovie;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRandomMovie extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerAdapterRandomMovie mRecyclerAdapterRandomMovie;
    ArrayList<Movie> mRandomMovies = new ArrayList<>();

    public FragmentRandomMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_movie, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_random_movie);
        mRecyclerAdapterRandomMovie = new RecyclerAdapterRandomMovie(getContext(), mRandomMovies, R.layout.item_random_movie);
        mRecyclerView.setAdapter(mRecyclerAdapterRandomMovie);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<RandomMovie> getRandomMovieCall = AppController.getFingoService()
                .getRandomMovie(AppController.getToken());

        getRandomMovieCall.enqueue(new Callback<RandomMovie>() {
            @Override
            public void onResponse(Call<RandomMovie> call, Response<RandomMovie> response) {
                if (response.isSuccessful()) {
                    RandomMovie randomMovie = response.body();
                    Log.e("log", "connection success");

                    for (int i=0; i<randomMovie.getData().size(); i++) {
                        mRandomMovies.add(randomMovie.getData().get(i));
                    }
                    //mRandomMovies.addAll(randomMovie.getData());

                    mRecyclerAdapterRandomMovie.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RandomMovie> call, Throwable t) {

            }
        });

        return view;
    }

}
