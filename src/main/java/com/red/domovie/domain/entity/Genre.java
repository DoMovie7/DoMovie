package com.red.domovie.domain.entity;

import lombok.RequiredArgsConstructor;

// Genre enum 클래스는 영화 장르를 정의합니다.
// @RequiredArgsConstructor 어노테이션을 사용하여 final 필드를 초기화하는 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
public enum Genre {

    ACTION("액션"), // 액션 장르
    MELO_ROMANCE("멜로/로맨스"), // 멜로/로맨스 장르
    DRAMA("드라마"), // 드라마 장르
    HORROR("공포"), // 공포 장르
    COMEDY("코미디"), // 코미디 장르
    ANIMATION("애니메이션"), // 애니메이션 장르
    SF_FANTASY("SF/판타지"), // SF/판타지 장르
    ETC("기타"); // 기타 장르

    private final String koName; // 장르의 한글 이름

    // 각 장르의 한글 이름을 반환하는 메서드입니다.
    public String koName() {
        return koName;
    }
}
