package com.red.domovie.domain.dto.movieDetail;


import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;


@Builder
@Getter
@ToString
public class GetMovieDetailDTO {
    private String title; //제목
    private String repRlsDate; //개봉일
    private String genre; //카테고리
    private String nation;//제작국가
    private String runtime;//러닝타임
    private String rating;//관람가
    private String company;//제작사
    private String plotText;//줄거리
    private String vodUrl; //메인 예고편 URL
    private List<StaffDTO> staffs; //감독 및 출연 배우 리스트
    private String posters; //포스터 이미지 URL

    public static GetMovieDetailDTO toDTO(JsonNode node) {
        if (node == null || !node.has("Data") || node.get("Data").size() == 0 
            || !node.get("Data").get(0).has("Result") || node.get("Data").get(0).get("Result").size() == 0) {
            throw new IllegalArgumentException("Invalid JSON structure");
        }

        JsonNode movieData = node.get("Data").get(0).get("Result").get(0);

        List<StaffDTO> staffs = new ArrayList<>();
        JsonNode staffNodes = movieData.path("staffs").path("staff");
        if (staffNodes.isArray()) {
            for (JsonNode staff : staffNodes) {
                String roleGroup = staff.path("staffRoleGroup").asText();
                if ("감독".equals(roleGroup) || "출연".equals(roleGroup)) {
                    staffs.add(StaffDTO.from(
                        staff.path("staffNm").asText(),
                        roleGroup,
                        staff.path("staffEnNm").asText()
                    ));
                }
            }
        }

        String vodUrl = "";
        JsonNode vods = movieData.path("vods").path("vod");
        if (vods.isArray()) {
            for (JsonNode vod : vods) {
                if (vod.path("vodClass").asText().contains("메인예고편")) {
                    vodUrl = vod.path("vodUrl").asText();
                    break;
                }
            }
        }

        return GetMovieDetailDTO.builder()
                .title(movieData.path("title").asText())
                .repRlsDate(movieData.path("repRlsDate").asText())
                .genre(movieData.path("genre").asText())
                .nation(movieData.path("nation").asText())
                .runtime(movieData.path("runtime").asText())
                .rating(movieData.path("rating").asText())
                .company(movieData.path("company").asText())
                .plotText(movieData.path("plots").path("plot").get(0).path("plotText").asText())
                .vodUrl(vodUrl)
                .staffs(staffs)
                .posters(movieData.path("posters").asText())
                .build();
    }
}
