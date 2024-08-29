package com.red.domovie.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Genre {
	
	ACTION("액션"),
	MELO_ROMANCE("멜로/로맨스"),
	DRAMA("드라마"),
	HORROR("공포"),
	COMEDY("코메디"),
	ANIMATION("애니메이션"),
	SF_FANTASY("SF/판타지"),
	ETC("기타");
	
	final String koName;
	public String koName() {return koName;}
	
}
