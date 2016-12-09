package com.teamfingo.android.fingo.category;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.teamfingo.android.fingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategory extends Fragment {

    ImageButton mImageButton1;
    ImageButton mImageButton2;
    ImageButton mImageButton3;


    public FragmentCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mImageButton1 = (ImageButton) view.findViewById(R.id.imageButton1);
        mImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentMovieList();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                transaction.replace(R.id.main_container, fragment);
                transaction.commit();
            }
        });

        return view;
    }

}
