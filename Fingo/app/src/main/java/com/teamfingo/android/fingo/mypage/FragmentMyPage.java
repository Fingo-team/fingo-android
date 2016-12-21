package com.teamfingo.android.fingo.mypage;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.util.KakaoParameterException;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.login.ActivityLogin;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.model.UserDetail;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.EndlessRecyclerOnScrollListener;

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
    LinearLayoutManager mLayoutManager;

    LoginManager mFacebookLoginManager;
    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    AccessToken mAccessToken;

    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    Uri imageUri;

    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    private static final int MY_PAGE_COMMENT = 0;
    private static final int MY_PAGE_WISH = 1;
    private static final int MY_PAGE_WATCHED = 2;

    private static final int INIT_PAGE = 1;

    // 이미지 세팅 요청 코드
    private static final int REQ_CODE_TAKE_PHOTO = 0;
    private static final int REQ_CODE_SELECT_IMAGE = 1;
    private static final int REQ_CODE_FACEBOOK_IMAGE = 2;

    // 업로드 할 이미지 유형
    private static final int UPLOAD_PROFILE = 0;
    private static final int UPLOAD_COVER = 1;

    private int upload_type;

    public FragmentMyPage() throws KakaoParameterException {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        // Mypage layout component init
        initView(view);

        // Mypage contents data load
        // Profile data load
        callFingoUserProfile();

        // User comments data load
        callFingoUserComments(INIT_PAGE);

        // inflate user comments in mypage
        initRecyclerView(view);

        return view;
    }

    private void initView(View view) {

        btnMyPageSetting = (ImageButton) view.findViewById(R.id.button_mypage_setting);
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

        tvCommentCount = (TextView) view.findViewById(R.id.textView_comment_count);
        tvWishCount = (TextView) view.findViewById(R.id.textView_wish_count);
        tvWatchedCount = (TextView) view.findViewById(R.id.textView_watched_count);

        header = (RecyclerViewHeader) view.findViewById(R.id.header);
    }

    private void initRecyclerView(View view) {

        mUserComments = new ArrayList<>();

        // Standard Recycler View Setting
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_mypage_comment);
        mAdapter = new RecyclerAdapterMypageComment(this.getContext(), this.getActivity(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.e("Check Page", ">>>>>>>>" + current_page + "");
                callFingoUserComments(current_page);
            }
        };

        mRecyclerView.setLayoutManager(mLayoutManager);
        // Recycler view Header library call
        header.attachTo(mRecyclerView);
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    // User Profile 정보 요청 하는 메소드
    private void callFingoUserProfile() {

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
                            if (data.getUser_profile().getUser_img_url() == "")
                                Glide.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(ivProfile);
                            else
                                Glide.with(getActivity()).load(data.getUser_profile().getUser_img_url()).into(ivProfile);


                            // 유저 커버 이미지 세팅
                            if (data.getUser_profile().getCover_img_url() == "") {
                                Glide.with(getActivity()).load(R.drawable.image_profile_cover).into(ivProfileCover);
                                ivProfileCover.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                            } else {
                                Glide.with(getActivity()).load(data.getUser_profile().getCover_img_url()).into(ivProfileCover);
                                ivProfileCover.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                            }
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

    // User Comment 정보 요청 하는 메소드
    private void callFingoUserComments(int page) {

        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken(), page, "activity_time");
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

    // Fingo Token 삭제 요청 메소드
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 설정
            case R.id.button_mypage_setting:
                expireFingoToken();
                break;

            case R.id.button_mypage_add:
                openSettingMenu(v);
                break;

            // 프로필 이미지
            case R.id.image_profile:
                editProfileImage(v);
                break;

            // 프로필 커버 이미지
            case R.id.image_profile_cover:
                editProfileImage(v);
                break;

            // 코멘트 디테일 프레그먼트 호출
            case R.id.button_comment:
                sendFragment(MY_PAGE_COMMENT);
                break;

            // 보고싶어요 디테일 프레그먼트 호출
            case R.id.button_wish:
                sendFragment(MY_PAGE_WISH);
                break;

            // 봤어요 디테일 프레그먼트 호출
            case R.id.button_watched:
                sendFragment(MY_PAGE_WATCHED);
                break;
        }
    }

    // Sub Activity 로 fragment 정보를 전송해주는 메소드
    public void sendFragment(int fragment_id) {

        Intent intent = new Intent(getActivity(), ActivityMyPage.class);
        intent.putExtra("Fragment", fragment_id);
        startActivity(intent);
    }


    public void openSettingMenu(View view) {

        new BottomSheet.Builder(this.getActivity()).title("Settings").sheet(R.menu.item_android_bottom_menu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.menu_help:

                        break;

                    case R.id.menu_call:

                        break;

                    case R.id.menu_upload:

                        break;

                    case R.id.menu_share:

                        break;
                }
            }
        }).show();
    }

    public void editProfileImage(View view) {

        String menuTitle;

        if (view.getId() == R.id.image_profile) {
            menuTitle = "Profile Options";
            upload_type = UPLOAD_PROFILE;
        } else {
            menuTitle = "Cover Image Options";
            upload_type = UPLOAD_COVER;
        }

        new BottomSheet.Builder(this.getActivity())
                .title(menuTitle)
                .sheet(R.menu.item_profile_image)
                .listener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.menu_takePhoto:
                                setRuntimePermission();
                                Log.e("CHECK TYPE", upload_type + "");
                                takePhoto();
                                break;

                            case R.id.menu_getGallery:
                                Log.e("CHECK TYPE", upload_type + "");
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (data != null && resultCode == Activity.RESULT_OK) {

                String filePath = "";
                File file = null;
                Bitmap image_bitmap = null;
                String type;

                switch (requestCode) {

                    case REQ_CODE_TAKE_PHOTO:
                        image_bitmap = (Bitmap) data.getExtras().get("data");
                        type = data.getStringExtra("Type");
                        Log.e("CHECK TYPE", type + "");

                        imageUri = getImageUri(getActivity(), image_bitmap);
                        filePath = getRealPathFromURI(imageUri);

                        file = new File(filePath);
                        sendImage(file);

                        break;

                    case REQ_CODE_SELECT_IMAGE:

                        type = data.getStringExtra("Type");
                        Log.e("CHECK TYPE", type + "");

                        // Uri에서 이미지 이름을 얻어온다.
                        filePath = getRealPathFromURI(data.getData());

                        // 서버에 전송 할 multipart form data 를 생성
                        file = new File(filePath);
                        sendImage(file);

                        break;
                }
            } else
                Log.e("FAIL", "Fail to send image");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takePhoto() {

        // 사진 촬영을 할 때, 런타임 권한 요청을 하도록 설정

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // requestCode지정해서 인텐트 실행
        intent.putExtra("Type", "COVER");
        startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);

    }

    @Override
    public void getGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // 이미지만 필터링
        intent.putExtra("Type", "COVER");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE_SELECT_IMAGE);

    }

    @Override
    public void getFacebookImage() {

        if (AccessToken.getCurrentAccessToken().isExpired() == false) {
            Log.e("아아아", "끝이났다 왜 만료되는거냐아");
        }

        new GraphRequest(AccessToken.getCurrentAccessToken()    // 최근 Accesstoken 을 가져온다
                , "/{user-id}?fields={cover}"    // Graph API 에 필요한 쿼리문
                , null
                , HttpMethod.GET
                // 쿼리의 결과 값을 Callback 함수로 받아 온다.
                , new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {

                Log.e("CHECK Response", "User_Cover_image : " + response.getError());
            }
        }
        ).executeAsync();

//        mFacebookLoginManager

    }

    @Override
    public void deleteImage() {
        Glide.with(getContext()).load(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfile);
    }

    @Override
    public void sendImage(File file) {
        Log.e("send Image", "+++++++++" + file.getAbsolutePath());

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        if (upload_type == UPLOAD_PROFILE) {
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_img", file.getName(), reqFile);
            Call<ResponseBody> uploadImageCall = AppController.getFingoService().uploadImage(AppController.getToken(), body);
            uploadImageCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.e("CHECK API", response.message());
                        Log.e("http", response.code() + "profile");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else if (upload_type == UPLOAD_COVER) {
            MultipartBody.Part body = MultipartBody.Part.createFormData("cover_img", file.getName(), reqFile);
            Call<ResponseBody> uploadImageCall = AppController.getFingoService().uploadImage(AppController.getToken(), body);
            uploadImageCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.e("CHECK API", response.message());
                        Log.e("http", response.code() + "cover");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public String getRealPathFromURI(Uri contentUri) {

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);
        } catch (Exception e) {

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

    public void setRuntimePermission() {

        PermissionListener mPermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(getContext())
                .setPermissionListener(mPermissionListener)
                .setRationaleMessage("해당 서비스 이용을 위한 자원 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

}
