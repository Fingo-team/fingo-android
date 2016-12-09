package com.teamfingo.android.fingo.mypage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.teamfingo.android.fingo.R;

import java.util.ArrayList;


public class ActivityMyPage extends AppCompatActivity {

    ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        int fragment_id = getIntent().getIntExtra("Fragment", 0);

        mFragments.add(0, new FragmentCommentDetail());
        mFragments.add(1, new FragmentWishMovie());
        mFragments.add(2, new FragmentWatchedMovie());


        replaceFragment(mFragments.get(fragment_id));

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.activity_my_page, fragment);
        transaction.commit();

    }
}
