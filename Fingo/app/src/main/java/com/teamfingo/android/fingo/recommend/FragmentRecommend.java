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

import com.teamfingo.android.fingo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecommend extends Fragment {

    FragmentIncreaseMovieRating mFragmentIncreaseMovieRating;

    public FragmentRecommend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommand, container, false);

        mFragmentIncreaseMovieRating = new FragmentIncreaseMovieRating();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_recommend);
        tabLayout.addTab(tabLayout.newTab().setText("취향 통계"));
        tabLayout.addTab(tabLayout.newTab().setText("영화 평가 늘리기"));

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_recommend);
        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    break;
                case 1:
                    fragment = mFragmentIncreaseMovieRating;
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
