package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;


public class FragmentMyPage extends Fragment {

    private String BASE_URL = "http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/";

    public FragmentMyPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);



        return view;
    }

}
