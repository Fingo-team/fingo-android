package com.teamfingo.android.fingo.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.login.ActivityLogin;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.utils.FingoPreferences;

public class ActivitySplash extends AppCompatActivity {

    // SplashActivity 지속시간 설정
    private static int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final FingoPreferences pref = new FingoPreferences(this);
        Log.e("SPLASH", "---" + pref.getAccessToken());
        // postDelayed Handler 를 이용한 화면이동, 애니메이션, 지속시간 설정
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // @TODO 현재 화면에서 로그인 인증여부에 따라 로그인화면 또는 메인 화면으로 분기하도록 구현해야 함.
                if (pref.getAccessToken().length() > 10) {
                    Log.e("SPLASH_TOKEN_EXIST", "---" + pref.getAccessToken());
                    // splash Activity 에서 Main Activity 로 이동
                    Intent intent = new Intent(ActivitySplash.this, ActivityMain.class);
                    ActivitySplash.this.startActivity(intent);

                    // Activity 화면 이동 시 애니메이션 설정
                    overridePendingTransition(android.support.design.R.anim.abc_fade_in, android.support.design.R.anim.abc_fade_out);
                    finish();

                } else {
                    Log.e("SPLASH_TOKEN_NON_EXIST", "---" + pref.getAccessToken());
                    // splash Activity 에서 login Activity 로 이동
                    Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                    ActivitySplash.this.startActivity(intent);

                    // Activity 화면 이동 시 애니메이션 설정
                    overridePendingTransition(android.support.design.R.anim.abc_fade_in, android.support.design.R.anim.abc_fade_out);
                    finish();

                }

            }
            // 스플래시 화면 지속시간 설정
        }, SPLASH_DISPLAY_LENGTH);
    }

}

