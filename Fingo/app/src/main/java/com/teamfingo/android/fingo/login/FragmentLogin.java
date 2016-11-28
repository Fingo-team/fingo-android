package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.main.ActivityMain;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Login ==
 *
 * Email 또는 Facebook 을 이용한 로그인 기능 구현
 *
 */

public class FragmentLogin extends Fragment implements View.OnClickListener {

    // email 로그인과 facebook 로그인을 위한 버튼 선언
    Button btnEmailLogin;
    Button btnFacebookLogin;

    public FragmentLogin() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnEmailLogin = (Button) view.findViewById(R.id.button_email_login);
        btnEmailLogin.setOnClickListener(this);

        btnFacebookLogin = (Button) view.findViewById(R.id.button_facebook_login);
        btnFacebookLogin.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            // TODO facebook SDK 를 이용해 인증 할 수 있도록 구현 해야 함
            case R.id.button_facebook_login:

            // 버튼 클릭 시 ActivityMain 으로 이동
            case R.id.button_email_login:
                Intent intent = new Intent(getContext(), ActivityMain.class);
                startActivity(intent);

                break;
        }
    }
}
