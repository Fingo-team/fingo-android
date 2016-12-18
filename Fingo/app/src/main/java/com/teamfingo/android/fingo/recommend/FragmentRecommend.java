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

    public static final int TABCOUNT = 2;

    FragmentStatistics mFragmentStatistics;
    FragmentRandomMovie mFragmentRandomMovie;

    TabLayout mTabLayout;
    RelativeLayout mContainerRecommend;

    ViewPager mViewPager;

    public FragmentRecommend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommand, container, false);

        mContainerRecommend = (RelativeLayout) view.findViewById(R.id.container_recommend);

        mFragmentStatistics = new FragmentStatistics();
        mFragmentRandomMovie = new FragmentRandomMovie();

        // Initialize TabLayout
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout_recommend);
        mTabLayout.addTab(mTabLayout.newTab().setText("취향 통계"));
        mTabLayout.addTab(mTabLayout.newTab().setText("평가 늘리기"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initialie ViewPager
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager_recommend);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = mFragmentStatistics;
                    break;
                case 1:
                    fragment = mFragmentRandomMovie;
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return TABCOUNT;
        }
    }
}
