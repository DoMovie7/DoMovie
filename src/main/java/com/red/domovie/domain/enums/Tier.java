package com.red.domovie.domain.enums;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum Tier {
    CORN("옥수수", "/img/tier/Asset 1.png"),
    POPCORN("터진 옥수수", "/img/tier/Asset 2.png"),
    FULLPOPCORN("팝콘", "/img/tier/Asset 3.png");

    private final String desc;
    private final String url;
    
    public String desc() {
    	return desc;
    }
    public String url() {
    	return url;
    }
    
}