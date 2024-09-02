package com.red.domovie.domain.dto.movieDetail;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StaffDTO {
    private String name;
    private String role;
    private String englishName;

    public static StaffDTO from(String name, String role, String englishName) {
        return StaffDTO.builder()
                .name(name)
                .role(role)
                .englishName(englishName)
                .build();
    }
}