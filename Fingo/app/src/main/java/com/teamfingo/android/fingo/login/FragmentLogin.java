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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 * <p>
 * == Fragment Login ==
 * <p>
 * Email 또는 Facebook 을 이용한 로그인 기능 구현
 */

public class FragmentLogin extends Fragment implements View.OnClickListener {

    // email 로그인과 facebook 로그인을 위한 버튼 선언
    Button btnEmailLogin;
    Button btnFacebookLogin;

    EditText etEmail;
    EditText etPassword;

    String mEmail;
    String mPassword;

    CallbackManager mCallbackManager;

    public FragmentLogin() {
        // Required empty public constructor

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 페이스북 액티비티로부터 인증 결과를 전달 받기 위한 callback method
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // 로그인 화면 구성을 위한 layout component 초기화
        initView(view);

        return view;

    }

    private void initView(View view) {

        // 이메일 로그인을 위한 기본정보 작성
        etEmail = (EditText) view.findViewById(R.id.editText_Login_Email);  // 유저 이메일
        etPassword = (EditText) view.findViewById(R.id.editText_Login_Password);    // 유저 비밀번호

        // 이메일로 로그인 버튼 선언
        btnEmailLogin = (Button) view.findViewById(R.id.button_email_login);
        btnEmailLogin.setOnClickListener(this);

        // 페이스북으로 로그인 버튼 선언
        btnFacebookLogin = (Button) view.findViewById(R.id.button_facebook_login);
        btnFacebookLogin.setOnClickListener(this);
    }

    // 이메일 또는 페이스북으로 로그인하기 버튼을 클릭 했을 때의 로직 구현
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 1. 페이스북으로 로그인 구현
            case R.id.button_facebook_login:
                // 1.1 페이스북 액티비티의 결과 값을 전달 받을 callback manager 선언
                mCallbackManager = CallbackManager.Factory.create();
                // 1.2 페이스북 액티비티 요청
                facebookLoginOnClick();
                break;

            // 2. 이메일로 로그인 구현
            case R.id.button_email_login:

                // 2.1 이메일 로그인을 위한 유저 기본정보 입력
                mEmail = etEmail.getText().toString();  // 유저 이메일
                mPassword = etPassword.getText().toString();    // 유저 비밀번호

                requestFingoAccessToken();

                break;
        }
    }

    // CallFingoAPI Method Overloading
    // 이메일 로그인 할 때의 Fingo login API 호출
    private void requestFingoAccessToken() {

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().userEmailLogin(mEmail, mPassword);

        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if (response.isSuccessful()) {

                    FingoPreferences pref = new FingoPreferences(getContext());

                    Log.e("CHECK TOKEN_GET_BEFORE", ">>>>>>>>" + pref.getAccessToken());

                    String token = response.body().getToken();
                    pref.setAccessToken(token);

                    Log.e("CHECK TOKEN_GET_AFTER", ">>>>>>>>" + token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "로그인에 실패 하였습니다!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // CallFingoAPI Method Overloading
    // 페이스북 로그인 할 때의 Fingo login API 호출
    private void requestFingoAccessToken(String facebook_token) {

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createFacebookUser(facebook_token);
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if (response.isSuccessful()) {

                    FingoPreferences pref = new FingoPreferences(getContext());

                    Log.e("CHECK TOKEN_GET_BEFORE", ">>>>>>>>" + pref.getAccessToken());

                    String token = response.body().getToken();
                    pref.setAccessToken(token);

                    Log.e("CHECK TOKEN_GET_AFTER", ">>>>>>>>" + token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                    getActivity().finish();

                } else {

                    Toast.makeText(getContext(), "로그인에 실패 하였습니다!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void facebookLoginOnClick() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {
                requestFingoAccessToken(result.getAccessToken().getToken());
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                getActivity().finish();

            }

            @Override
            public void onCancel() {
                getActivity().finish();
            }
        });
    }

}
