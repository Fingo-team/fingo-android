package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.UserMovies;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWishMovie extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterMovie mAdapter;

    ArrayList<UserMovies.Results> mWishMovies = new ArrayList<>();

    private static final String BASE_URL = "http://fingo-dev.ap-northeast-2.elasticbeanstalk.com/";

    private FingoPreferences mPref;

    public FragmentWishMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_wish_movie, container, false);

        btnOrdering = (ImageButton) view.findViewById(R.id.button_ordering);
        tvOrdering = (TextView) view.findViewById(R.id.textView_ordering);

        mPref = new FingoPreferences(getContext());
        callFingoService();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_wish_movie);
        mAdapter = new RecyclerAdapterMovie(this.getContext(), mWishMovies);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager manager = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView.setLayoutManager(manager);

        return view;
    }

    private void callFingoService() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FingoService service = client.create(FingoService.class);
        Call<UserMovies> wishMovieCall = service.getWishMovie(mPref.getAccessToken());
        wishMovieCall.enqueue(new Callback<UserMovies>() {
            @Override
            public void onResponse(Call<UserMovies> call, Response<UserMovies> response) {
                if (response.isSuccessful()) {

                    UserMovies data = response.body();
                    for(UserMovies.Results movie : data.getResults()){
                        mWishMovies.add(movie);
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
