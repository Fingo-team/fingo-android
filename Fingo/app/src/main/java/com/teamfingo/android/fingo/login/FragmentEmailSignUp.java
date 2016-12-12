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

    EditText etUserName;
    EditText etEmail;
    EditText etPassword;
    
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_sign_up, container, false);


        // TODO Rx 사용해 볼 것 - 입력이 된 순간 text 에 입력 되도록!
        etUserName = (EditText) view.findViewById(R.id.editText_userName);
        etEmail = (EditText) view.findViewById(R.id.editText_email);
        etPassword = (EditText) view.findViewById(R.id.editText_password);

        btnSignUp = (Button) view.findViewById(R.id.button_signUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEmail = etEmail.getText().toString();
                mPassword = etPassword.getText().toString();
                mUserName = etUserName.getText().toString();

                callFingoAPI();
            }
        });

        return view;
    }

    private void callFingoAPI() {

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createEmailUser(mEmail, mPassword, mUserName);
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if(response.isSuccessful()){

                    Toast.makeText(getContext(), "회원 인증 메일을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    replaceFragment(new FragmentLoginMain());
                }
                else
                // TODO 어떤 정보의 중복으로 인해 회원가입이 되지 않는것인지 출력되는 메세지 세분화가 필요
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }
}
