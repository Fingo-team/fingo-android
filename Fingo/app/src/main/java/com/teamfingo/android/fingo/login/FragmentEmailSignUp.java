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
 * == Fragment Email SignUp ==
 *
 * 이메일을 이용한 가입을 위한 Fragment
 *
 */

public class FragmentEmailSignUp extends Fragment {


    //TODO 입력받은 사용자의 이메일로 인증 메일을 보낼 것인지, 이메일(ID)와 비밀번호를 서버로 POST 해 줄것인지를 결정 해야함.

    public FragmentEmailSignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_email_sign_up, container, false);
    }

}
