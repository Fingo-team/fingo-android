package com.teamfingo.android.fingo.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamfingo.android.fingo.R;


/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Login Main ==
 *
 * 로그인의 메인화면 역할을 한다.
 *
 * 기존사용자의 로그인과 신규회원의 회원가입화면으로 이동시켜준다.
 *
 */

public class FragmentLoginMain extends Fragment implements View.OnClickListener{

    Button btnLogIn;
    Button btnEmailSignUp;
    Button btnFacebookSigUp;

    Fragment fragmentLogin;
    Fragment fragmentEmailSignUp;
    Fragment fragmentFacebookSignUp;

    public FragmentLoginMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        fragmentLogin = new FragmentLogin();
        fragmentEmailSignUp = new FragmentEmailSignUp();
        fragmentFacebookSignUp = new FragmentFacebookSignUp();

        btnLogIn = (Button)view.findViewById(R.id.button_login);
        btnLogIn.setOnClickListener(this);

        btnEmailSignUp = (Button)view.findViewById(R.id.button_email_sign_up);
        btnEmailSignUp.setOnClickListener(this);

        btnFacebookSigUp = (Button)view.findViewById(R.id.button_facebook_sign_up);
        btnFacebookSigUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_login:
                replaceFragment(fragmentLogin);
                break;

            case R.id.button_email_sign_up:
                replaceFragment(fragmentEmailSignUp);
                break;

            case R.id.button_facebook_sign_up:
                replaceFragment(fragmentFacebookSignUp);
                break;
        }
    }

    // pager 를 쓰지않고 단순히 Fragment 를 하나의 Layout container 에서 변경 해주기 위한 method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }
}
