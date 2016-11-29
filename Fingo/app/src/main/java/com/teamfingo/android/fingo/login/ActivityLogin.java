package com.teamfingo.android.fingo.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.teamfingo.android.fingo.R;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Activity Login ==
 * 네 종류의 Fragment 들을 보여주기 위한 Container 역할
 *
 * == Facebook SDK ==
 * keystore password : fingo1221
 * facebook SDK Hash key : Qce+5cPoBrUhZu5LF5UFADzGUno=
 *
 */

public class ActivityLogin extends AppCompatActivity{

    // 로그인 메인 화면이 될 Fragment 선언
    Fragment fragmentLoginMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Freagment Login Main 이 생성되기 이전에 facebook SDK 를 초기화 함.
        FacebookSdk.sdkInitialize(getApplicationContext());

        // ActivityLogin 의 첫 화면을 FragmentLoginMain 으로 보여 준다.
        fragmentLoginMain = new FragmentLoginMain();
        replaceFragment(fragmentLoginMain);

    }

    // pager 를 쓰지않고 단순히 Fragment 를 하나의 Layout container 에서 변경 해주기 위한 method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }
}
