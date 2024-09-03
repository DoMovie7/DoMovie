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
	private String movieId; //아이디
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
    private String poster; //포스터 이미지 URL
    private String stll;
    

    public static GetMovieDetailDTO toDTO(JsonNode node) {
       

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
        
        
        

        return GetMovieDetailDTO.builder()
        		.movieId(movieData.path("DOCID").asText())
                .title(movieData.path("title").asText())
                .repRlsDate(formatDate(movieData.path("repRlsDate").asText()))
                .genre(movieData.path("genre").asText())
                .nation(movieData.path("nation").asText())
                .runtime(formatRuntime(movieData.path("runtime").asText()))
                .rating(movieData.path("rating").asText())
                .company(movieData.path("company").asText())
                .plotText(movieData.path("plots").path("plot").get(0).path("plotText").asText())
                .vodUrl(movieData.path("vods").path("vod").get(0).path("vodUrl").asText())
                .staffs(staffs)
                .poster(movieData.path("posters").asText().split("\\|")[0])
                .stll(movieData.path("stlls").asText().split("\\|")[1])
                .build();
    }
    
    
    //개봉 날짜 포멧
    private static String formatDate(String date) {
        if (date.length() != 8) {
            return date; // 예외 처리: 형식이 맞지 않으면 원본 반환
        }
        return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6);
    }
    
    //상영 시간 포멧
    private static String formatRuntime(String runtime) {
        try {
            int minutes = Integer.parseInt(runtime);
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            return String.format("%d시간 %d분", hours, remainingMinutes);
        } catch (NumberFormatException e) {
            return runtime; // 파싱 실패 시 원본 값 반환
        }
    }
    
}
