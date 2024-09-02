package com.red.domovie.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.movie.BoxOfficeDTO;
import com.red.domovie.domain.dto.movie.KmdbMovieResponse;
import com.red.domovie.service.MovieApiService;

@Service
public class MovieApiServiceProcess implements MovieApiService {

    @Value("${kobis.api.key}")
    private String kobisApiKey;
    
    @Value("${kmdb.api.key}")
    private String kmdbApiKey;
    
    private final String BOXOFFICE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private final String NEW_LIST_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";

    @Override
    public void getBoxOffice(Model model) {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder(BOXOFFICE_LIST_API_URL);
        urlBuilder.append("?key=").append(kobisApiKey);
        urlBuilder.append("&targetDt=").append(formattedDate); // 날짜는 YYYYMMDD 형식으로 입력해야 함

        String url = urlBuilder.toString();
        List<BoxOfficeDTO> boxOfficeDTOs = new ArrayList<>();
        try {
            // 박스오피스 API 호출
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject boxOfficeResult = jsonResponse.getJSONObject("boxOfficeResult");
            JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("dailyBoxOfficeList");
            
            // BoxOfficeDTO 리스트 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // 각 영화 정보를 BoxOfficeDTO로 변환
            for (int i = 0; i < dailyBoxOfficeList.length(); i++) {
                JSONObject movie = dailyBoxOfficeList.getJSONObject(i);
                BoxOfficeDTO boxOfficeDTO = objectMapper.readValue(movie.toString(), BoxOfficeDTO.class);
                
                // KMDB API를 통해 포스터 매핑
                String poster = fetchPosterFromKMDB(boxOfficeDTO.getMovieNm());
                boxOfficeDTO.setPosters(poster);
                
                boxOfficeDTOs.add(boxOfficeDTO);
            }
            model.addAttribute("list", boxOfficeDTOs);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    // 영화명을 이용하여 KMDB에서 포스터 이미지 검색
    private String fetchPosterFromKMDB(String movieNm) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(NEW_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&title=").append(movieNm);
        strBuilder.append("&listCount=1");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("Data") && jsonResponse.getJSONArray("Data").length() > 0) {
                JSONArray dataArray = jsonResponse.getJSONArray("Data");
                JSONObject firstData = dataArray.getJSONObject(0);
                JSONArray resultArray = firstData.getJSONArray("Result");
                if (resultArray.length() > 0) {
                    JSONObject movieData = resultArray.getJSONObject(0);
                    return movieData.optString("posters", "").split("[|]")[0]; // 첫 번째 포스터만 반환
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; // 포스터가 없거나 에러 발생 시 빈 문자열 반환
    }

    @Override
    public void getNewMovies(Model model) {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(NEW_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&releaseDts=").append(formattedDate);
        strBuilder.append("&listCount=10");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        
        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("Data") && jsonResponse.getJSONArray("Data").length() > 0) {
                JSONArray dataArray = jsonResponse.getJSONArray("Data");
                JSONObject firstData = dataArray.getJSONObject(0);
                //System.out.println(">>>>:" + firstData);
                
                ObjectMapper objectMapper = new ObjectMapper();
                KmdbMovieResponse kmdbMovieResponse = objectMapper.readValue(firstData.toString(), new TypeReference<KmdbMovieResponse>() {});
                //System.out.println("***:" + kmdbMovieResponse);
                model.addAttribute("list", kmdbMovieResponse.getResult());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
