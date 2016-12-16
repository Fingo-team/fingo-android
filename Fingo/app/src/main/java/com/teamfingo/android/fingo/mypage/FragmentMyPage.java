package com.teamfingo.android.fingo.mypage;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.login.ActivityLogin;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.model.UserDetail;
import com.teamfingo.android.fingo.utils.AppController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.teamfingo.android.fingo.utils.FingoPreferences.removeAccessToken;

public class FragmentMyPage extends Fragment implements View.OnClickListener, setProfileImage {

    ImageButton btnMyPageSetting, btnMyPageAdd;
    ImageView ivProfile, ivProfileCover;
    TextView tvUserName, tvUserIntroduce, tvCommentCount, tvWishCount, tvWatchedCount;
    Button btnComment, btnWish, btnWatched;

    RecyclerView mRecyclerView;
    RecyclerAdapterMypageComment mAdapter;
    RecyclerViewHeader header;

    Uri imageUri;

    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

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

        btnMyPageSetting = (ImageButton) view.findViewById(R.id.button_mypage_setting);
        btnMyPageSetting.setOnClickListener(this);

        btnMyPageAdd = (ImageButton) view.findViewById(R.id.button_mypage_add);
        btnMyPageAdd.setOnClickListener(this);

        ivProfile = (ImageView) view.findViewById(R.id.image_profile);
        Glide.with(this.getActivity()).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(ivProfile);
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

        tvCommentCount = (TextView) view.findViewById(R.id.textView_comment_count);
        tvWishCount = (TextView) view.findViewById(R.id.textView_wish_count);
        tvWatchedCount = (TextView) view.findViewById(R.id.textView_watched_count);

        header = (RecyclerViewHeader) view.findViewById(R.id.header);

        callFingoService();
        callFingoComment();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_mypage_comment);
        mAdapter = new RecyclerAdapterMypageComment(this.getContext(), this.getActivity(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        header.attachTo(mRecyclerView);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_mypage_setting:
                expireFingoToken();
                break;

            case R.id.button_mypage_add:
                openSettingMenu(v);
                break;

            case R.id.image_profile:
                editProfileImage(v);
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

    public void openSettingMenu(View view) {

        new BottomSheet.Builder(this.getActivity()).title("Profile options").sheet(R.menu.item_android_bottom_menu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.menu_help:
                        // TODO when help menu/button is clicked
                        break;

                    case R.id.menu_call:
                        // TODO when call menu/button is clicked
                        break;

                    case R.id.menu_upload:
                        // TODO when upload menu/button is clicked
                        break;

                    case R.id.menu_share:
                        // TODO when share menu/button is clicked
                        break;
                }
            }
        }).show();
    }

    public void editProfileImage(View view) {

        new BottomSheet.Builder(this.getActivity())
                .title("Profile Setting")
                .sheet(R.menu.item_profile_image)
                .listener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.menu_takePhoto:
                                takePhoto();
                                break;

                            case R.id.menu_getGallery:

                                getGallery();
                                break;

                            case R.id.menu_facebookProfile:
                                getFacebookImage();
                                break;

                            case R.id.menu_delete:
                                deleteImage();
                                break;
                        }
                    }
                }).show();
    }

    public void sendFragment(int fragment_id) {

        Intent intent = new Intent(getActivity(), ActivityMyPage.class);
        intent.putExtra("Fragment", fragment_id);
        startActivity(intent);
    }

    private void expireFingoToken() {

        Call<Void> fingoLogoutCall = AppController.getFingoService().userEmailLogout(AppController.getToken());
        fingoLogoutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.e("Check Login Status", ">>>>>>>>" + response.message());

                if (response.isSuccessful()) {

                    removeAccessToken();
                    Log.e("CHECK_TOKEN_AFTER", AppController.getToken());
                    Log.e("Check Login Status", ">>>> 로그아웃 성공!!");

                    Intent intent = new Intent(getActivity(), ActivityLogin.class);
                    startActivity(intent);
                    getActivity().finish();

                } else
                    Log.e("Check Login Status", ">>>> 로그아웃 실패!!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void callFingoService() {

        Call<UserDetail> userDetailCall = AppController.getFingoService().getUserDetail(AppController.getToken());
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.isSuccessful()) {
                    final UserDetail data = response.body();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            tvUserName.setText(data.getUser_profile().getNickname());
                            tvCommentCount.setText(data.getComment_cnt());
                            tvWishCount.setText(data.getWish_movie_cnt());
                            tvWatchedCount.setText(data.getWatched_movie_cnt());

                            // 유저 프로필 이미지 세팅
                            if (data.getUser_profile().getUser_img() == null)
                                Glide.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(ivProfile);

                            else{}
//                                Glide.with(getActivity()).load(data.getUser_profile().getUser_img()).into(ivProfile);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void callFingoComment() {

        // TODO 싱글톤으로 좀 더 깔끔하게 표현 가능
        // 호출 될 때마다 초기화
        mUserComments = new ArrayList<>();

        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken());
        userCommentsCall.enqueue(new Callback<UserComments>() {
            @Override
            public void onResponse(Call<UserComments> call, Response<UserComments> response) {
                if (response.isSuccessful()) {

                    UserComments data = response.body();
                    for (UserComments.Results comment : data.getResults()) {
                        mUserComments.add(comment);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserComments> call, Throwable t) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        ivProfile.setImageBitmap(imageBitmap);
        imageUri = getImageUri(getActivity(),imageBitmap);
        String filePath = getRealPathFromURI(imageUri);
//        imageUri = data.getData();
//        Log.e("check uri", imageUri+"");
//
//        String filePath = getRealPathFromURI(imageUri);
//
        File file = new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        Call<ResponseBody> uploadImageCall = AppController.getFingoService().uploadImage(AppController.getToken(), body, name);
        uploadImageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("Upload", "success");
                }

                else
                    Log.e("Upload", "Fail");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }

    @Override
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }

    @Override
    public void getGallery() {

    }

    @Override
    public void getFacebookImage() {

    }

    @Override
    public void deleteImage() {

    }

    @Override
    public String getRealPathFromURI(Uri contentUri){

        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);
        }catch (Exception e){

            e.printStackTrace();
            return contentUri.getPath();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

}
