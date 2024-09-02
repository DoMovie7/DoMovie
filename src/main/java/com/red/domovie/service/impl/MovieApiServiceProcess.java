package com.red.domovie.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.red.domovie.domain.dto.movie.MovieDTO;
import com.red.domovie.service.MovieApiService;

@Service
public class MovieApiServiceProcess implements MovieApiService {

    @Value("${kobis.api.key}")
    private String kobisApiKey;
    
    @Value("${kmdb.api.key}")
    private String kmdbApiKey;
    
    private final String BOXOFFICE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private final String MOVIE_LIST_API_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    //private final String NEW_LIST_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";
    private final String NEW_LIST_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";

    //일일 박스오피스
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
                
                // KMDB API를 통해 포스터와 영화 ID 매핑
                Map<String, String> posterData = fetchPosterFromKMDB(boxOfficeDTO.getMovieNm(), boxOfficeDTO.getOpenDt());
                boxOfficeDTO.setPosters(posterData.get("poster")); // 포스터 URL 설정
                boxOfficeDTO.setDOCID(posterData.get("DOCID"));; // 영화 ID 설정
                
                boxOfficeDTOs.add(boxOfficeDTO);
            }
            model.addAttribute("list", boxOfficeDTOs);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


 // 영화명, 개봉일을 이용하여 KMDB에서 포스터 이미지 검색
    private Map<String, String> fetchPosterFromKMDB(String movieNm, String releaseDts) {
        final String DEFAULT_POSTER = "/img/index/no-movie-img.jpg";
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(NEW_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&title=").append(movieNm);
        
        if (releaseDts != null && !releaseDts.isEmpty()) {
            strBuilder.append("&releaseDts=").append(releaseDts);
        }

        strBuilder.append("&listCount=1");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        String url = strBuilder.toString();

        Map<String, String> result = new HashMap<>();
        result.put("poster", DEFAULT_POSTER);
        result.put("DOCID", "");
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("Data") && jsonResponse.getJSONArray("Data").length() > 0) {
                JSONArray dataArray = jsonResponse.getJSONArray("Data");
                JSONObject firstData = dataArray.getJSONObject(0);

                if (firstData.has("Result") && firstData.getJSONArray("Result").length() > 0) {
                    JSONArray resultArray = firstData.getJSONArray("Result");
                    JSONObject movieData = resultArray.getJSONObject(0);

                    String poster = movieData.optString("posters", "").split("[|]")[0]; // 첫 번째 포스터만 반환
                    String docid = movieData.optString("DOCID", ""); // 영화 ID

                    result.put("poster", poster.isEmpty() ? DEFAULT_POSTER : poster);
                    result.put("DOCID", docid);
                    
                } else {
                    System.out.println("Result array is missing or empty in the JSON response.");
                }
            } else {
                System.out.println("Data array is missing or empty in the JSON response.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    // 개봉일기준 최신영화
    @Override
    public void getNewMovies(Model model) {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedDate = date.format(formatter);

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder(MOVIE_LIST_API_URL);
        urlBuilder.append("?key=").append(kobisApiKey);
        urlBuilder.append("&openEndDt=").append(formattedDate);
        urlBuilder.append("&itemPerPage=10");

        String url = urlBuilder.toString();
        List<MovieDTO> movieDTOs = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject movieListResult = jsonResponse.getJSONObject("movieListResult");
            JSONArray movieList = movieListResult.getJSONArray("movieList");

            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < movieList.length(); i++) {
                JSONObject movie = movieList.getJSONObject(i);
                //System.out.println("<<<>>>" + movie.toString());
                MovieDTO movieDTO = objectMapper.readValue(movie.toString(), MovieDTO.class);

                // KMDB API를 통해 포스터와 영화 ID 매핑
                Map<String, String> posterData = fetchPosterFromKMDB(movieDTO.getMovieNm(), movieDTO.getOpenDt());
                movieDTO.setPoster(posterData.get("poster"));
                movieDTO.setDOCID(posterData.get("DOCID"));
                movieDTOs.add(movieDTO);
            }
            
            // 영화 목록을 개봉일 기준으로 최신순 정렬
            movieDTOs.sort(Comparator.comparing(MovieDTO::getOpenDt).reversed());
            
            model.addAttribute("list", movieDTOs);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //개봉일 최신순, 장르 공포영화
    
    @Override
    public void getHorrorMovies(Model model) {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(NEW_LIST_API_URL);
        strBuilder.append("&detail=y");
        strBuilder.append("&genre=공포");
        strBuilder.append("&releaseDte=").append(formattedDate);
        strBuilder.append("&listCount=10");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");

        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.has("Data") && jsonResponse.getJSONArray("Data").length() > 0) {
                JSONArray dataArray = jsonResponse.getJSONArray("Data");
                JSONObject firstData = dataArray.getJSONObject(0);
                System.out.println(">>>>:" + firstData);

                ObjectMapper objectMapper = new ObjectMapper();
                KmdbMovieResponse kmdbMovieResponse = objectMapper.readValue(firstData.toString(), new TypeReference<KmdbMovieResponse>() {});

                System.out.println("***:" + kmdbMovieResponse);
                model.addAttribute("list", kmdbMovieResponse.getResult());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
}
