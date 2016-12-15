package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.teamfingo.android.fingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecommend extends Fragment {

    FragmentIncreaseMovieRating mFragmentIncreaseMovieRating;
    FragmentStatistics mFragmentStatistics;
    RelativeLayout containerRecommend;

    public FragmentRecommend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommand, container, false);

        containerRecommend = (RelativeLayout) view.findViewById(R.id.container_recommend);

        mFragmentStatistics = new FragmentStatistics();
        mFragmentIncreaseMovieRating = new FragmentIncreaseMovieRating();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_recommend);
        replaceFragment(mFragmentStatistics);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(mFragmentStatistics);
                        break;
                    case 1:
                        replaceFragment(mFragmentIncreaseMovieRating);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.container_recommend, fragment);
        transaction.commit();
    }
}
