package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-12-07
 *
 * == Fragment Comment Detail ==
 *
 * 코멘트 세부 정보를 볼 수 있는 fragment
 *
 */

public class FragmentCommentDetail extends Fragment implements InterfaceFragmentMypageDetail {

    // 레이아웃 구성
    // 화면 새로고침을 위한 layout
    SwipeRefreshLayout mSwipeRefreshLayout;

    // Recycler view 구성
    RecyclerView mRecyclerView;
    RecyclerAdapterCommentDetail mAdapter;
    LinearLayoutManager mLayoutManager;

    // Recycler view pagination 을 위한 Listener
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    // 정렬 종류를 선택 할 수 있는 Spinner
    Spinner mSpinner;

    // FingoService.getUserComments 로 부터 받아오는 코멘트 상세 정보
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    // Pagination 을 위한 환경변수 설정
    private static final int INIT_PAGE = 1; // 최초 페이지 로드

    // Sorting 을 위한 환경변수 설정
    private static final int SORT_TIME = 0;     // 작성 순
    private static final int SORT_TITLE = 1;    // 가나다 순
    private static final int SORT_SCORE = 2;    // 평점 순

    public FragmentCommentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);

        initView(view);
        loadData(INIT_PAGE, "activity_time");
        initRecyclerView(view);
        setActions();

        return view;
    }


    @Override
    public void initView(View view){

        // 화면 새로고침을 위한 swipeRefresh layout 선언
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_commentDetail);

        // Spinner component 들을 R.array.movie_sorting 으로 부터 가져와 Spinner item 에 부착
        String[] str = getResources().getStringArray(R.array.movie_sorting);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, str);

        // 코멘트 정렬을 위한 spinner 선언
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setAdapter(adapter);

    }

    @Override
    public void loadData(int page, String order) {

        // 1. 서버로 부터 UserComment 내역을 호출
        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken(), page, order);

        // 2. Response data (UserComments) get
        userCommentsCall.enqueue(new Callback<UserComments>() {

            // 2.1 통신에 성공 했을 경우,
            @Override
            public void onResponse(Call<UserComments> call, Response<UserComments> response) {
                if (response.isSuccessful()) {

                    // 2.1.1 코멘트 데이터를 하나씩 받아 mUserComments 에 삽입
                    UserComments data = response.body();
                    for (UserComments.Results comment : data.getResults()) {
                        mUserComments.add(comment);
                    }
                }
                // 2.1.2 이후 데이터 셋에 변경 사항이 생겼을 때, 이를 적용하기 위한 리스너 부착
                mAdapter.notifyDataSetChanged();
            }

            // 2.2 통신에 실패 했을 경우,
            @Override
            public void onFailure(Call<UserComments> call, Throwable t) {
                // 2.2.1 오류사항 출력
                t.printStackTrace();
            }
        });
    }

    @Override
    public void initRecyclerView(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_comment_detail);
        mAdapter = new RecyclerAdapterCommentDetail(this.getContext(), this.getActivity(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void sortingMovie(int position){
        switch(position){

            // 1. 작성 순 정렬
            case SORT_TIME:
                loadData(INIT_PAGE, "activity_time");
                break;

            // 2. 가나다 순 정렬
            case SORT_TITLE:
                loadData(INIT_PAGE, "title");
                break;

            // 3. 평점 순 정렬 (유저가 직접 매긴 평점 순)
            case SORT_SCORE:
                loadData(INIT_PAGE, "score");
                break;
        }
    }

    @Override
    public void setActions(){

        // 1. 화면 내 새로고침 동작을 위한 swipeRefreshListener 구현
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 1.1 새로고침 동작이 입력되면 데이터 초기화
                mUserComments.clear();

                // 1.2 작성 순(기본 값) 으로 정렬된 데이터 입력
                // TODO 새로고침 전 정렬 상태를 전달받아 새로고침을 진행 할 수 있도록 구현해야 함.
                loadData(INIT_PAGE, "activity_time");

                // 1.3 화면 새로고침 진행
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // 2. 정렬 기준 선택 동작을 위한 spinner itemSelectListener 구현
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 2.1 스피너의 아이템이 제대로 선택 되었을 경우,
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 2.1.1 정렬기준이 선택 되면 기존 데이터들을 초기화
                mUserComments.clear();

                // 2.1.2 선택 된 정렬기준(position) 에 따라 Comment Data 재 세팅
                sortingMovie(position);
            }

            // 2.2 스피너의 아이템이 제대로 선택 되지 않았을 경우,
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 3. Comment Pagination 을 위한 RecyclerView Scroll Listener 구현
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {

            // 3.1 스크롤의 끝을 인식 했을 때,
            @Override
            public void onLoadMore(int current_page) {
                // 3.1.1 서버로부터 다음 페이지의 데이터들을 불러온다.
                // TODO Comment 데이터 전체가 정렬되는것이 아닌 페이지별로 재 정렬 되는 문제 발생
                loadData(current_page, "activity_time");
            }
        };
        // 3.2 생성 된 스크롤 리스너를 리사이클러 뷰에 부착
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }
}
