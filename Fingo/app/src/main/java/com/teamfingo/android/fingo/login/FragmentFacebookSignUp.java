package com.teamfingo.android.fingo.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamfingo.android.fingo.R;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Facebook SignUp ==
 *
 * Facebook을 이용한 가입을 위한 Fragment
 *
 */

public class FragmentFacebookSignUp extends Fragment {

    //TODO Facebook API 로 부터 전달받은 토큰을 클라이언트와 서버에서 어떻게 관리 해 줄 것인지에 관해 논의 필요

    public FragmentFacebookSignUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_facebook_sign_up, container, false);
    }

}
