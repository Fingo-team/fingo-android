package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.teamfingo.android.fingo.R;


public class FragmentMyPage extends Fragment {

    Button btnFacebookLogout;

    public FragmentMyPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        btnFacebookLogout = (Button) view.findViewById(R.id.button_facebook_logout);
        btnFacebookLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 페이스북 로그아웃을 위한 메소드 호출
                LoginManager.getInstance().logOut();
                Log.e("Check Facebook Logout", "Logout Successfully!!");

            }
        });
        return view;
    }

}
