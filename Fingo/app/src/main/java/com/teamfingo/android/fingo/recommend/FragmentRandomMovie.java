package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRandomMovie extends Fragment {

    RecyclerView mRecyclerView;

    public FragmentRandomMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_increase_movie_rating, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_increase_movie_rating);
        return view;
    }

}
