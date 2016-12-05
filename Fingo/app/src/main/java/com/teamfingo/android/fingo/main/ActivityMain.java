package com.teamfingo.android.fingo.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RelativeLayout;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.utils.FingoPreferences;
import com.teamfingo.android.fingo.category.FragmentCategory;
import com.teamfingo.android.fingo.home.FragmentHome;
import com.teamfingo.android.fingo.mypage.FragmentMyPage;
import com.teamfingo.android.fingo.recommend.FragmentRecommend;

public class ActivityMain extends AppCompatActivity {

    FragmentHome mFragmentHome;
    FragmentCategory mFragmentCategory;
    FragmentRecommend mFragmentRecommend;
    FragmentMyPage mFragmentMyPage;

    TabLayout mTabLayout;
    RelativeLayout container;
    FingoPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

        mFragmentHome = new FragmentHome();
        mFragmentCategory = new FragmentCategory();
        mFragmentRecommend = new FragmentRecommend();
        mFragmentMyPage = new FragmentMyPage();

        mPref = new FingoPreferences(this);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        container = (RelativeLayout) findViewById(R.id.container);

        Log.e("CHECK_TOKEN_MAIN","======================" + mPref.getAccessToken());
        replaceFragment(mFragmentHome);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("aaaa", "aaaa");

                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(mFragmentHome);
                        break;
                    case 1:
                        replaceFragment(mFragmentCategory);
                        break;
                    case 2:
                        replaceFragment(mFragmentRecommend);
                        break;
                    case 3:
                        replaceFragment(mFragmentMyPage);
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        Log.d("aaaa", "aaaa");

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
