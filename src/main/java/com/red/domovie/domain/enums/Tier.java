package com.red.domovie.domain.enums;

import lombok.Getter;

@Getter
public enum Tier {
    CORN("옥수수", 0, 2),
    POPCORN("터진 옥수수", 3, 5),
    FULLPOPCORN("팝콘", 6, Integer.MAX_VALUE);

    private final String description;
    private final int minPostCount;
    private final int maxPostCount;

    Tier(String description, int minPostCount, int maxPostCount) {
        this.description = description;
        this.minPostCount = minPostCount;
        this.maxPostCount = maxPostCount;
    }

    public static Tier fromPostCount(int postCount) {
        for (Tier tier : values()) {
            if (postCount >= tier.minPostCount && postCount <= tier.maxPostCount) {
                return tier;
            }
        }
        return FULLPOPCORN; // 기본값 또는 예외 처리
    }
}