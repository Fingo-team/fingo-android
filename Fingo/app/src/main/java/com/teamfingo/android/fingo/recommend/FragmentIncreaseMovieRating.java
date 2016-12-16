package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIncreaseMovieRating extends Fragment {


    public FragmentIncreaseMovieRating() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_increase_movie_rating, container, false);
    }

}