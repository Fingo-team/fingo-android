package com.teamfingo.android.fingo.mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserMovies;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWatchedMovie extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterMovie mAdapter;
    GridLayoutManager mLayoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    ArrayList<UserMovies.Results> mWatchedMovies = new ArrayList<>();

    private static final int INIT_PAGE = 1;

    public FragmentWatchedMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watched_movie, container, false);

        initView(view);

        callUserMovies(INIT_PAGE);

        initRecyclerView(view);

        return view;
    }

    private void initView(View view){

        btnOrdering = (ImageButton) view.findViewById(R.id.button_ordering);
        tvOrdering = (TextView) view.findViewById(R.id.textView_ordering);
    }

    private void initRecyclerView(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_watched_movie);
        mAdapter = new RecyclerAdapterMovie(this.getContext(), mWatchedMovies);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(this.getContext(), 3);
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                callUserMovies(current_page);
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void callUserMovies(int page) {

        Call<UserMovies> watchedMovieCall = AppController.getFingoService().getWatchedMovie(AppController.getToken(), page);
        watchedMovieCall.enqueue(new Callback<UserMovies>() {
            @Override
            public void onResponse(Call<UserMovies> call, Response<UserMovies> response) {
                if (response.isSuccessful()) {
                    UserMovies data = response.body();
                    for (UserMovies.Results movie : data.getResults()) {
                        mWatchedMovies.add(movie);
                        Log.e("check",">>>>>"+movie.getMovie().getTitle());
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserMovies> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
