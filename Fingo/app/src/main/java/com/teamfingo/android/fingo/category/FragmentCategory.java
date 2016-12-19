package com.teamfingo.android.fingo.category;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.utils.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategory extends Fragment {

    ImageView mImageViewCategory;

    public FragmentCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mImageViewCategory = (ImageView) view.findViewById(R.id.imageView_category);

        Call<Movie> getMovieCall = AppController.getFingoService()
                .getMovie(AppController.getToken(), "2"); // 라라랜드

        getMovieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();

                    Movie.Stillcut[] stillCutImg = movie.getStillcut();

                    Glide.with(getContext()).load(stillCutImg[0].getImg()).into(mImageViewCategory);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("log", "Categort fail ==== " + t.getMessage());
                t.printStackTrace();
            }
        });

        mImageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentMovieList();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment);
                transaction.commit();
            }
        });

        return view;
    }

}
