package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.utils.FingoPreferences;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.model.FingoAccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

    EditText etEmail;
    EditText etPassword;

    String mEmail;
    String mPassword;

    private String BASE_URL = "http://fingo-dev.ap-northeast-2.elasticbeanstalk.com/";

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

        etEmail = (EditText) view.findViewById(R.id.editText_Login_Email);
        etPassword = (EditText) view.findViewById(R.id.editText_Login_Password);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            // TODO facebook SDK 를 이용해 인증 할 수 있도록 구현 해야 함
            case R.id.button_facebook_login:

            // 버튼 클릭 시 ActivityMain 으로 이동
            case R.id.button_email_login:

                mEmail = etEmail.getText().toString();
                mPassword = etPassword.getText().toString();

                callFingoAPI();

                break;
        }
    }

    private void callFingoAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.e("CHECK!!", "=========="+mEmail);
        Log.e("CHECK!!", "=========="+mPassword);

        FingoService fingoService = retrofit.create(FingoService.class);
        Call<FingoAccessToken> fingoAccessTokenCall = fingoService.userEmailLogin(mEmail, mPassword);

        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if(response.isSuccessful()){

                    FingoPreferences pref = new FingoPreferences(getContext());

                    Log.e("CHECK TOKEN_GET_BEFORE", ">>>>>>>>"+pref.getAccessToken());

                    String token = response.body().getToken();
                    pref.setAccessToken(token);

                    Log.e("CHECK TOKEN_GET_AFTER", ">>>>>>>>" + token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                    getActivity().finish();
                }
                else
                    Toast.makeText(getContext(), "로그인에 실패 하였습니다!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


}
