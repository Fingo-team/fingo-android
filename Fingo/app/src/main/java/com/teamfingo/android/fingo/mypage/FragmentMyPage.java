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

import org.json.JSONException;
import org.json.JSONObject;

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
    TextView tvUserName, tvCommentCount, tvWishCount, tvWatchedCount;
    Button btnComment, btnWish, btnWatched;

    RecyclerView mRecyclerView;
    RecyclerAdapterMypageComment mAdapter;
    RecyclerViewHeader header;
    LinearLayoutManager mLayoutManager;

    // 페이스북 서비스 이용을 위한 manager 들
    // TODO 페이스북으로 가입된 유저가 아닌 경우, 추가 인증 요청 기능 구현 필요
    LoginManager mFacebookLoginManager;
    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    AccessToken mAccessToken;

    // 마이페이지 메인에서 보여질 코멘트들의 infinite scroll
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    // 프로필, 커버사진 업로드를 위한 이미지 uri
    Uri imageUri;

    // 유저 작성 코멘트 정보를 담는 ArrayList
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    // Mypage Detail 화면으로 분기 하기위한 요청 코드
    private static final int MY_PAGE_COMMENT = 0;
    private static final int MY_PAGE_WISH = 1;
    private static final int MY_PAGE_WATCHED = 2;

    // 페이지 초기화 상수
    private static final int INIT_PAGE = 1;

    // 인텐트를 이용한 이미지 세팅 요청 코드
    private static final int REQ_CODE_TAKE_PHOTO = 0;
    private static final int REQ_CODE_SELECT_IMAGE = 1;

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

        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        // Mypage View 초기화
        initView(view);

        // Mypage 에 필요한 data set load
        // 유저 프로필 정보 load
        callFingoUserProfile();

        // 유저 코멘트 정보 load
        callFingoUserComments(INIT_PAGE);

        // 유저가 작성한 코멘트 출력
        initRecyclerView(view);

        return view;
    }

    private void initView(View view) {

        // 로그아웃 버튼
        // TODO 로그아웃 기능 이외에 추가 기능 구현 필요
        btnMyPageSetting = (ImageButton) view.findViewById(R.id.button_mypage_setting);
        btnMyPageSetting.setOnClickListener(this);

        // 마이페이지 추가기능 버튼
        // TODO 추가 기능 기획 필요
        btnMyPageAdd = (ImageButton) view.findViewById(R.id.button_mypage_add);
        btnMyPageAdd.setOnClickListener(this);

        // 유저 프로필
        ivProfile = (ImageView) view.findViewById(R.id.image_profile);
        ivProfile.setOnClickListener(this);

        // 유저 커버 이미지
        ivProfileCover = (ImageView) view.findViewById(R.id.image_profile_cover);
        ivProfileCover.setOnClickListener(this);

        // 유저 닉네임
        tvUserName = (TextView) view.findViewById(R.id.textView_user_nickname);

        // 코멘트 상세 페이지 이동 버튼
        btnComment = (Button) view.findViewById(R.id.button_comment);
        btnComment.setOnClickListener(this);

        // 보고싶어요 상세 페이지 이동 버튼
        btnWish = (Button) view.findViewById(R.id.button_wish);
        btnWish.setOnClickListener(this);

        // 봤어요 상세 페이지 이동 버튼
        btnWatched = (Button) view.findViewById(R.id.button_watched);
        btnWatched.setOnClickListener(this);

        // 코멘트, 보고싶어요, 봤어요에 대한 카운트 - Mypage 메인에서 확인가능
        tvCommentCount = (TextView) view.findViewById(R.id.textView_comment_count);
        tvWishCount = (TextView) view.findViewById(R.id.textView_wish_count);
        tvWatchedCount = (TextView) view.findViewById(R.id.textView_watched_count);

        // 유저의 기본 정보(프로필,커버,닉네임 등...)을 담고 있는 Recycler View Header
        header = (RecyclerViewHeader) view.findViewById(R.id.header);
    }

    // 유저 코멘트 정보를 보여주는 Recycler View
    private void initRecyclerView(View view) {
        // 1. 기존 코멘트 정보 초기화
        mUserComments = new ArrayList<>(); // 페이지가 로드 될때마다 코멘트 데이터를 초기화 합니다.

        // 2. Recycler View 기본 구성 세팅
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_mypage_comment);
        mAdapter = new RecyclerAdapterMypageComment(this.getContext(), this.getActivity(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this.getContext());

        // 3. 구성 된 Recycler View 에 Infinite scroll 구현
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                callFingoUserComments(current_page);
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 4. Recycler View 에 추가 기능들을 부착
        // 4.1 Recycler View Header 부착
        header.attachTo(mRecyclerView);
        // 4.2 Scroll Listener 부착
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    // User Profile 정보 요청 하는 메소드
    private void callFingoUserProfile() {

        // 1. 서버에 유저 프로필 정보 Request
        Call<UserDetail> userDetailCall = AppController.getFingoService().getUserDetail(AppController.getToken());
        // 2. 서버로부터 Response data 받음
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                // 2.1 성공적으로 데이터를 리턴 받았다면,
                if (response.isSuccessful()) {
                    // 2.2 Handler 를 이용해 View 에 받은 데이터들을 load
                    final UserDetail data = response.body();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {

                            // 2.2.1 유저 기본 정보 로드
                            tvUserName.setText(data.getUser_profile().getNickname());   // 닉네임
                            tvCommentCount.setText(data.getComment_cnt());  // 코멘트 카운트
                            tvWishCount.setText(data.getWish_movie_cnt());  // 보고싶어요 카운트
                            tvWatchedCount.setText(data.getWatched_movie_cnt());    // 봤어요 카운트

                            // 2.2.2 유저 프로필 이미지 세팅
                            // 서버에 프로필이미지가 있는지 없는지를 판단
                            if (data.getUser_profile().getUser_img_url() == "")
                                Glide.with(getActivity()).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(ivProfile);
                            else
                                Glide.with(getActivity()).load(data.getUser_profile().getUser_img_url()).into(ivProfile);


                            // 2.2.3 유저 커버 이미지 세팅
                            // 서버에 프로필이미지가 있는지 없는지를 판단
                            // 프로필 컨텐츠의 가독성을 위한 커버이미지 shader 추가
                            if (data.getUser_profile().getCover_img_url() == "") {
                                Glide.with(getActivity()).load(R.drawable.image_profile_cover).into(ivProfileCover);
                                ivProfileCover.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);   // shader
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

        // 1. 서버에 유저 코멘트 정보 Request
        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken(), page, "activity_time");
        // 2. 서버로 부터 Response 데이터 받음
        userCommentsCall.enqueue(new Callback<UserComments>() {
            @Override
            public void onResponse(Call<UserComments> call, Response<UserComments> response) {
                // 2.1 성공적으로 Response 데이터를 받았다면,
                if (response.isSuccessful()) {

                    // 2.1.1 전달받은 comment 정보를 페이지 내의 ArrayList 에 삽입
                    UserComments data = response.body();
                    for (UserComments.Results comment : data.getResults()) {
                        mUserComments.add(comment);
                    }
                    // 2.1.2 이후 코멘트 데이터 셋에 변화가 있을경우 이를 새로고침함.
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserComments> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // Fingo Token 삭제 요청 메소드
    private void expireFingoToken() {

        // 1. 서버에 토큰 삭제 Request
        Call<Void> fingoLogoutCall = AppController.getFingoService().userEmailLogout(AppController.getToken());
        // 2. 서버로부터 Response 받음.
        fingoLogoutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 2.1 토큰 삭제가 성공적으로 이루어 졌다면,
                if (response.isSuccessful()) {
                    // 2.1.1 App 내의 Shared Preferences 에 저장된 Fingo Token 을 삭제
                    removeAccessToken();

                    Log.e("CHECK_TOKEN_AFTER", AppController.getToken());
                    Log.e("Check Login Status", ">>>> 로그아웃 성공!!");

                    // 2.1.2 서버와 앱내의 토큰이 삭제 된 후, 초기 로그인 화면으로 분기
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

    // Mypage 화면 내의 버튼 클릭 동작 정의
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 로그아웃
            case R.id.button_mypage_setting:
                // 토큰 만료 요청
                expireFingoToken();
                break;

            // 추가기능
            case R.id.button_mypage_add:
                openSettingMenu(v);
                break;

            // 프로필 이미지
            case R.id.image_profile:
                // 1. Runtime permission 설정
                setRuntimePermission();
                // 2. 프로필 이미지 세팅을 위한 Bottom Sheet 호출
                editProfileImage(v);
                break;

            // 프로필 커버 이미지
            case R.id.image_profile_cover:
                // 1. Runtime permission 설정
                setRuntimePermission();
                // 2. 커버 이미지 세팅을 위한 Botton Sheet 호출
                editProfileImage(v);
                break;

            // 코멘트 디테일 프레그먼트 호출
            case R.id.button_comment:
                // 코멘트 상세 페이지로 분기
                sendFragment(MY_PAGE_COMMENT);
                break;

            // 보고싶어요 디테일 프레그먼트 호출
            case R.id.button_wish:
                // 보고싶어요 상세 페이지로 분기
                sendFragment(MY_PAGE_WISH);
                break;

            // 봤어요 디테일 프레그먼트 호출
            case R.id.button_watched:
                // 봤어요 상세 페이지로 분기
                sendFragment(MY_PAGE_WATCHED);
                break;
        }
    }

    // Sub Activity 로 fragment 정보를 전송해주는 메소드
    public void sendFragment(int fragment_id) {
        // 전달받은 Fragment_id 에 맞는 화면 호출
        Intent intent = new Intent(getActivity(), ActivityMyPage.class);
        intent.putExtra("Fragment", fragment_id);
        startActivity(intent);
    }

    // TODO 미구현 - 마이페이지 추가기능에 대한 기획이 필요
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

    // 프로필 또는 커버이미지 세팅을 위한 Bottom Sheet View 호출
    public void editProfileImage(View view) {

        // 1. 프로필 또는 커버이미지 여부 판단
        String menuTitle;
        // 1.1 프로필 이미지 변경
        if (view.getId() == R.id.image_profile) {
            // 1.1.1 Bottom Sheet 의 Title 설정
            menuTitle = "Profile Options";
            // 1.1.2 동작 Flag 값을 프로필 변경으로 설정
            upload_type = UPLOAD_PROFILE;
        }
        // 1.2 커버 이미지 변경
        else {
            // 1.2.1 Bottom Sheet 의 Title 설정
            menuTitle = "Cover Image Options";
            // 1.2.2 동작 flag 값을 커버이미지 변경으로 설정
            upload_type = UPLOAD_COVER;
        }

        // 2. Flag 값에 맞는 Bottom Sheet 호출
        new BottomSheet.Builder(this.getActivity())
                .title(menuTitle)
                .sheet(R.menu.item_profile_image)
                .listener(new DialogInterface.OnClickListener() {

                    // 2.1 이미지 변경을 위한 Bottom Sheet menu 구성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // 2.1.1 카메라로 사진 찍기
                            case R.id.menu_takePhoto:
                                takePhoto();
                                break;
                            // 2.1.2 갤러리나 외부 앨범 앱으로 부터 사진 가져오기
                            case R.id.menu_getGallery:
                                getGallery();
                                break;
                            // 2.1.3 페이스북 이미지 가져오기
                            // TODO 페이스북 로그인 여부에 따른 기능 분기 필요
                            case R.id.menu_facebookProfile:
                                getFacebookImage();
                                break;
                            // 2.1.4 기본 이미지로 초기화
                            case R.id.menu_delete:
                                deleteImage();
                                break;
                        }
                    }
                }).show();
    }

    // 카메라 또는 갤러리로 부터 이미지를 전달받는 경우
    // Intent 로부터 이미지를 전달 받는다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (data != null && resultCode == Activity.RESULT_OK) {

                // 1. 전달받은 이미지 처리를 위한 기본 데이터셋 세팅
                String filePath = "";   // 이미지 파일 경로
                File file = null;       // Multipart Form 데이터 전달을 위한 파일
                Bitmap image_bitmap = null; // 최초로 받아오는 이미지를 Bitmap 으로 세팅

                // 2. 인텐트 요청 코드에 따른 동작 분기
                switch (requestCode) {
                    // 2.1 카메라 Response
                    case REQ_CODE_TAKE_PHOTO:
                        // 2.1.1 받아온 이미지를 bitmap 으로 변환
                        image_bitmap = (Bitmap) data.getExtras().get("data");
                        // 2.1.2 bitmap 이미지를 jpeg 으로 변환
                        imageUri = getImageUri(getActivity(), image_bitmap);
                        // 2.1.3 변환된 이미지의 절대경로를 가져옴
                        filePath = getRealPathFromURI(imageUri);

                        // 2.1.4 절대경로를 바탕으로 파일을 만들고 이를 서버에 전달
                        file = new File(filePath);  // 파일생성
                        sendImage(file);    // 이미지를 서버에 전달

                        break;

                    // 2.2 갤러리 Response
                    case REQ_CODE_SELECT_IMAGE:
                        // 2.2.1 전달받은 이미지의 절대경로를 가져옴
                        filePath = getRealPathFromURI(data.getData());

                        // 2.2.2 절대경로를 바탕으로 파일을 만들고 이를 서버에 전달
                        file = new File(filePath);  // 파일생성
                        sendImage(file);    // 이미지를 서버에 전달

                        break;
                }
            } else
                // 3. 이미지 전달에 실패 했을경우 에러메세지 출력
                Log.e("FAIL", "Fail to send image");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 안드로이드 내장 카메라 호출
    @Override
    public void takePhoto() {
        // 1. 내장 카메라 호출
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 2. requestCode 지정해서 인텐트 실행
        startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);

    }

    // 갤러리앱 호출
    @Override
    public void getGallery() {
        // 1. 하나의 컨텐츠를 pick 할 수 있도록 설정
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 2. 이미지만 필터링
        intent.setType("image/*");
        // 3. requestCode 지정해서 인텐트 실행
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE_SELECT_IMAGE);

    }

    // 페이스북 이미지 호출 - 여러가지 Graph api 호출 방법 사용
    @Override
    public void getFacebookImage() {

        // 1. 커버 사진을 요청하는 경우
        if (upload_type == UPLOAD_COVER) {
            // 1.1 로그인 시 발급 받은 AccessToken 을 기반으로 Graph API 호출
            // request url : .../me/?field=cover
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                // 1.1.1 Json Object (Cover) 의 Field (source) 를 가져옴.
                                String mFacebookUrl = object.getJSONObject("cover").getString("source");
                                Log.e("CHECK IMAGE", "++++++++++++++++++++++" + mFacebookUrl);
                                // 1.1.2 서버에 이미지 url 전달
                                sendFacebookImage(mFacebookUrl);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            // 1.2 Graph api 에 Request Parameter 전달
            Bundle parameters = new Bundle();
            parameters.putString("fields", "cover");
            request.setParameters(parameters);
            request.executeAsync();

        }

        // 2. 프로필 사진을 요청하는 경우
        else if (upload_type == UPLOAD_PROFILE) {
            Bundle params = new Bundle();
            // 2.1 Graph API 로 부터 받아올 수 있는 기본 유저 정보 값 세팅
            params.putString("fields", "id,email,gender,cover,picture.type(large)");
            // 2.2 로그인 시 발급 받은 AccessToken 을 기반으로 Graph API 호출
            // request url : .../me/?field=id,email,gender,cover,picture.type(large)  또는 .../me/picture
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                try {
                                    // 2.1.1 Json object 로 부터 picture(프로필이미지) 에 관한 데이터 필드 가져옴.
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        String mFacebookUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        // 2.1.2 서버에 이미지 url 전달
                                        sendFacebookImage(mFacebookUrl);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).executeAsync();
        }

    }

    // 유저의 프로필과 커버이미지를 초기화
    @Override
    public void deleteImage() {

        if(upload_type == UPLOAD_PROFILE){}
        else if(upload_type == UPLOAD_COVER){}

    }

    // 카메라와 갤러리로 부터 전달받은 이미지를 서버에 전송
    @Override
    public void sendImage(File file) {
        Log.e("send Image", "+++++++++" + file.getAbsolutePath());
        // 1. Request body 에 들어갈 파일 삽입
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // 2. 동작 Flag 에 따른 데이터 전달 분기
        // 2.1 프로필 이미지 업로드
        if (upload_type == UPLOAD_PROFILE) {
            // 2.1.1 multipart form data 에 프로필 이미지임을 알리는 flag 설정
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_img", file.getName(), reqFile);
            // 2.1.2 서버에 업로드 Request 보냄
            Call<ResponseBody> uploadImageCall = AppController.getFingoService().uploadImage(AppController.getToken(), body);
            // 2.1.3 서버로부터 Response 받음
            uploadImageCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // 2.1.3.1 S3 서버에 제대로 이미지가 올라갔는지 판별
                        Log.e("CHECK API", response.message());
                        Log.e("http", response.code() + "profile");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        // 2.2 커버이미지 업로드
        else if (upload_type == UPLOAD_COVER) {
            // 2.2.1 multipart form data 에 커버이미지임을 알리는 flag 설정
            MultipartBody.Part body = MultipartBody.Part.createFormData("cover_img", file.getName(), reqFile);
            // 2.2.2 서버에 업로드 request 보냄
            Call<ResponseBody> uploadImageCall = AppController.getFingoService().uploadImage(AppController.getToken(), body);
            // 2.2.3 서버로부터 Response 받음
            uploadImageCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // 2.2.3.1 S3 서버에 제대로 이미지가 올라갔는지 판별
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

    // 페이스북 이미지 url 을 서버로 전달
    @Override
    public void sendFacebookImage(String url) {
        Log.e("CHECK UPLOAD", "++++++++++++++++++++++" + url);

        // 1. 동작 flag 에 따라 서로 다른 upload Request 전달
        // 1.1 프로필 사진 전달일 경우,
        if (upload_type == UPLOAD_PROFILE) {
            // 1.1.1 서버에 upload request 보냄
            Call<ResponseBody> uploadFacebookProfileCall = AppController.getFingoService().uploadFacebookProfile(AppController.getToken(), url);
            // 1.1.2 서버로부터 response 받음
            uploadFacebookProfileCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // 1.1.2.1 유저 기본 정보에 url 이 잘 적용 되었는지 확인
                        Log.e("CHECK API", response.message());
                        Log.e("http", response.code() + "profile");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
        // 1.2 커버 사진 전달일 경우,
        else if (upload_type == UPLOAD_COVER) {
            // 1.2.1 서버에 upload request 보냄
            Call<ResponseBody> uploadFacebookCoverCall = AppController.getFingoService().uploadFacebookCover(AppController.getToken(), url);
            // 1.2.2 서버로부터 response 받음
            uploadFacebookCoverCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 1.2.2.1 유저 기본정보에 url 이 잘 적용 되었는지 확인
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

    // 카메라나 갤러리로부터 전달받은 이미지의 절대 경로를 가져오는 메소드
    @Override
    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {

            e.printStackTrace();
            return contentUri.getPath();
        }
    }

    // 전달받은 비트맵 이미지를 jpeg 형식으로 변환하는 메소드
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

    // Runtime Permission 을 위한 권한 설정
    public void setRuntimePermission() {

        // 1. Runtime permission 을 위한 외부 라이브러리 호출(TedRuntimePermission)
        PermissionListener mPermissionListener = new PermissionListener() {
            // 1.1 이미 사용이 허용된 서비스에 접근 했을 경우 동작 정의
            @Override
            public void onPermissionGranted() {

            }
            // 1.2 사용이 거부된 서비스를 출력
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        // 2. 서비스 접근 제한이 걸려 있는 경우 Alert Dialog 출력
        new TedPermission(getContext())
                .setPermissionListener(mPermissionListener)
                .setRationaleMessage("해당 서비스 이용을 위한 자원 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
