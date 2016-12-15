package com.teamfingo.android.fingo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.search.ActivitySearch;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.FingoPreferences;
import com.teamfingo.android.fingo.category.FragmentCategory;
import com.teamfingo.android.fingo.home.FragmentHome;
import com.teamfingo.android.fingo.mypage.FragmentMyPage;
import com.teamfingo.android.fingo.recommend.FragmentRecommend;

public class ActivityMain extends AppCompatActivity {

    FragmentHome fragmentHome;
    FragmentCategory fragmentCategory;
    FragmentRecommend fragmentRecommend;
    FragmentMyPage fragmentMyPage;

    TabLayout tabLayout;
    RelativeLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_tool_bar));

        fragmentHome = new FragmentHome();
        fragmentCategory = new FragmentCategory();
        fragmentRecommend = new FragmentRecommend();
        fragmentMyPage = new FragmentMyPage();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mainContainer = (RelativeLayout) findViewById(R.id.main_container);

        Log.e("CHECK_TOKEN_MAIN","======================" + AppController.getToken());
        replaceFragment(fragmentHome);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("aaaa", "aaaa");

                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(fragmentHome);
                        break;
                    case 1:
                        replaceFragment(fragmentCategory);
                        break;
                    case 2:
                        replaceFragment(fragmentRecommend);
                        break;
                    case 3:
                        replaceFragment(fragmentMyPage);
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
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // ToolBar의 Search 아이콘을 눌렀을 때 ActivitySearch가 실행된다.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icon_search:
                Intent intent = new Intent(ActivityMain.this, ActivitySearch.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        //android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }
}
