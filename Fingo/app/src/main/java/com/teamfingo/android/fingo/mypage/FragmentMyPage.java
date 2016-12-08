package com.teamfingo.android.fingo.mypage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;


public class FragmentMyPage extends Fragment implements View.OnClickListener {

    ImageButton btnMyPageSetting, btnMyPageAdd;
    ImageView ivProfile, ivProfileCover;
    TextView tvUserName, tvUserIntroduce;
    Button btnComment, btnWish, btnWatched;

    LinearLayout mLinearLayout;

    public static final int MY_PAGE_COMMENT = 0;
    public static final int MY_PAGE_WISH = 1;
    public static final int MY_PAGE_WATCHED = 2;

    public FragmentMyPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        btnMyPageSetting = (ImageButton)view.findViewById(R.id.button_mypage_setting);
        btnMyPageSetting.setOnClickListener(this);

        btnMyPageAdd = (ImageButton) view.findViewById(R.id.button_mypage_add);
        btnMyPageAdd.setOnClickListener(this);

        ivProfile = (ImageView) view.findViewById(R.id.image_profile);
        ivProfile.setOnClickListener(this);

        ivProfileCover = (ImageView) view.findViewById(R.id.image_profile_cover);
        ivProfileCover.setOnClickListener(this);

        tvUserName = (TextView) view.findViewById(R.id.textView_user_nickname);
        tvUserIntroduce = (TextView) view.findViewById(R.id.textView_user_introduce);

        btnComment = (Button) view.findViewById(R.id.button_comment);
        btnComment.setOnClickListener(this);

        btnWish = (Button) view.findViewById(R.id.button_wish);
        btnWish.setOnClickListener(this);

        btnWatched = (Button) view.findViewById(R.id.button_watched);
        btnWatched.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_mypage_setting:

                break;

            case R.id.button_mypage_add:

                break;

            case R.id.image_profile:

                break;

            case R.id.image_profile_cover:

                break;

            case R.id.button_comment:
                sendFragment(MY_PAGE_COMMENT);
                break;

            case R.id.button_wish:
                sendFragment(MY_PAGE_WISH);
                break;

            case R.id.button_watched:
                sendFragment(MY_PAGE_WATCHED);
                break;
        }
    }

    public void sendFragment(int fragment_id){

        Intent intent = new Intent(getActivity(), ActivityMyPage.class);
        intent.putExtra("Fragment", fragment_id);
        startActivity(intent);
    }

    private void expireFingoToken(){

    }
}
