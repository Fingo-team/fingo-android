package com.teamfingo.android.fingo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by taewon on 2016-12-02.
 */

public class GetAccessToken {

    // 값 불러오기
    public static String getAccessToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("Token", "");
    }
}
