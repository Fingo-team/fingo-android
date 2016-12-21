package com.teamfingo.android.fingo.mypage;

import android.view.View;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-12-07
 *
 * == Fragment Mypage Detail Presenter ==
 *
 * MyPage 에서 제공하는 서비스인 '코멘트', '보고싶어요', '봤어요' 기능에 필요한 Presenter Interface 입니다.
 *
 * FragmentCommentDetail, FragmentWishMovie, FragmentWatchedMovie 에 적용되는 인터페이스 입니다.
 *
 */

public interface InterfaceFragmentMypageDetail {

    // 기본 레이아웃 세팅
    // parameter    : view - fragment inflating 을 위한 view 를 전달
    void initView(View view);

    // role         : 리사이클러 뷰에 담을 데이터셋 호출
    // parameter    : page - pagination 을 위한 페이지 번호, order - 정렬 순서
    void loadData(int page, String order);

    // role         : 데이터셋을 바탕으로 RecyclerView 에 아이템 Inflate
    // parameter    : view - fragment inflating 을 위한 view 를 전달
    void initRecyclerView(View view);

    // role         : 아이템 정렬 로직
    // parameter    : position - spinner 에서 선택된 item 의 포지션(정렬 방식)
    void sortingMovie(int position);

    // role         : 레이아웃 내 세부 아이템 액션 정의
    void setActions();

}
