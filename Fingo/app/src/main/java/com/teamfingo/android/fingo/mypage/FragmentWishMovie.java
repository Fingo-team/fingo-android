package com.teamfingo.android.fingo.mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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
public class FragmentWishMovie extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterMovie mAdapter;
    GridLayoutManager mLayoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    Spinner mSpinner;

    ArrayList<UserMovies.Results> mWishMovies = new ArrayList<>();

    private static final int INIT_PAGE = 1;

    private static final int SORT_TIME = 0;
    private static final int SORT_TITLE = 1;
    private static final int SORT_SCORE = 2;

    public FragmentWishMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_wish_movie, container, false);

        initView(view);

        callUserMovies(INIT_PAGE,"");

        initRecyclerView(view);

        return view;
    }

    private void initView(View view){

        String[] str = getResources().getStringArray(R.array.movie_sorting);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this.getContext(), android.R.layout.simple_spinner_dropdown_item, str);
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWishMovies.clear();
                sortingMovie(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_wish_movie);
        mAdapter = new RecyclerAdapterMovie(this.getContext(), mWishMovies);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(this.getContext(), 3);

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                callUserMovies(current_page, "");
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void callUserMovies(int page, String order) {

        Call<UserMovies> wishMovieCall = AppController.getFingoService().getWishMovie(AppController.getToken(), page, order);
        wishMovieCall.enqueue(new Callback<UserMovies>() {
            @Override
            public void onResponse(Call<UserMovies> call, Response<UserMovies> response) {
                if (response.isSuccessful()) {

                    UserMovies data = response.body();
                    for(UserMovies.Results movie : data.getResults()){
                        mWishMovies.add(movie);
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

    private void sortingMovie(int position){
        switch(position){

            case SORT_TIME:
                callUserMovies(INIT_PAGE, "activity_time");
                break;

            case SORT_TITLE:
                callUserMovies(INIT_PAGE, "title");
                break;

            case SORT_SCORE:
                callUserMovies(INIT_PAGE, "score");
                break;
        }
    }

}
