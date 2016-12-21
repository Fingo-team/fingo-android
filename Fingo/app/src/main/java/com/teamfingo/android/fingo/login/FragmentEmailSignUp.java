package com.teamfingo.android.fingo.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.utils.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Email SignUp ==
 *
 * 이메일을 이용한 가입을 위한 Fragment
 */

public class FragmentEmailSignUp extends Fragment {

    // 이메일 회원 가입을 위한 기본 정보 입력 창
    EditText etUserName;    // 유저 닉네임
    EditText etEmail;       // 유저 이메일
    EditText etPassword;    // 유저 비밀번호

    Button btnSignUp;

    String mEmail;
    String mPassword;
    String mUserName;

    public FragmentEmailSignUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_sign_up, container, false);

        // 1. 회원 가입 view 초기화
        initView(view);

        // 2. 회원 가입 진행
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 2.1 회원가입 요청을 위한 데이터 입력
                mEmail = etEmail.getText().toString();
                mPassword = etPassword.getText().toString();
                mUserName = etUserName.getText().toString();

                // 2.2 서버로 회원 가입 요청
                requestFingoAccessToken();
            }
        });

        return view;
    }

    // 이메일 회원 가입 창의 view 초기화
    private void initView(View view) {

        etUserName = (EditText) view.findViewById(R.id.editText_userName);
        etEmail = (EditText) view.findViewById(R.id.editText_email);
        etPassword = (EditText) view.findViewById(R.id.editText_password);

        btnSignUp = (Button) view.findViewById(R.id.button_signUp);

    }

    // 기입 된 회원가입 정보를 바탕으로 FingoToken 요청
    private void requestFingoAccessToken() {

        // 1. 기입 정보를 바탕으로 회원 가입 request
        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createEmailUser(mEmail, mPassword, mUserName);

        // 2. response data 확인
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if (response.isSuccessful()) {
                    // 2.1 회원가입에 성공 했을 경우, 기입한 이메일로 인증 절차를 거치도록 유도
                    Toast.makeText(getContext(), "회원 인증 메일을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    replaceFragment(new FragmentLoginMain());
                } else
                    // TODO 어떤 정보의 중복으로 인해 회원가입이 되지 않는것인지 출력되는 메세지 세분화가 필요
                    // 2.2 이미 존재 하는 아이디 이거나 부적절한 비밀 번호의 경우 사용자에게 재입력 요청
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 회원가입에 성공하면 로그인 메인 페이지로 이동
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }
}
