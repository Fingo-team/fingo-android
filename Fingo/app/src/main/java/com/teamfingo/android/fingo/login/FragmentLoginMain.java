package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    CallbackManager mCallbackManager;

    public FragmentLoginMain() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        fragmentLogin = new FragmentLogin();
        fragmentEmailSignUp = new FragmentEmailSignUp();

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

                // Fragment 로 구현 할 때에는 Callback Manager 를 private 으로 선언 하면 안됨.
                // FacebookSdk.sdkInitialize(getApplicationContext()); 의 위치가 매우 중요
                // 프레그먼트의 생명주기와 이벤트 생성 순서를 고려하여 facebook SDK 를 초기화 해주어야 함.
                // 때문에 이 경우에는 현재 프레그먼트를 담고 있는 ActivityLogin 의 onCreate 에서 SDK 를 초기화 해 주었음.
                mCallbackManager = CallbackManager.Factory.create();
                facebookLoginOnClick();
                break;
        }
    }

    // pager 를 쓰지않고 단순히 Fragment 를 하나의 Layout container 에서 변경 해주기 위한 method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }

    public void facebookLoginOnClick(){

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.i("TAG", "user: " + user.toString());
                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
//                            setResult(RESULT_OK);

                            callFingoAPI(result.getAccessToken().getToken());

                            // TODO 현재 프레그먼트가 속한 ActivityLogin 에서 ActivitiyMain 으로 이동 한 뒤에 현재 Activity 를 닫아 줄 수 있도록 구현 해야함.
//                            finish();
                        }
                    }
                });

                // TODO GraphRequest 의 onComplete 함수와 아래의 로직 간의 동작 순서를 다시 알아 볼 필요가 있음.
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });
    }

    private void callFingoAPI(String facebook_token) {

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createFacebookUser(facebook_token);
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if(response.isSuccessful()){

                    String token = response.body().getToken();
                    new FingoPreferences(getContext()).setAccessToken(token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

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

}
