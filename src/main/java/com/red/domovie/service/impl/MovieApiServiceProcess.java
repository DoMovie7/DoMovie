package com.red.domovie.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.red.domovie.service.MovieApiService;

@Service
public class MovieApiServiceProcess implements MovieApiService{

    @Value("${kobis.api.key}")
    private String apiKey;

    private final String MOVIE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    private final String BOXOFFICE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";

    public String getAllMovies() {
        RestTemplate restTemplate = new RestTemplate();
        String url = MOVIE_LIST_API_URL + "?key=" + apiKey;
        
        // API 호출 및 결과 반환
        return restTemplate.getForObject(url, String.class);
    }

	@Override
	public String getboxOffice() {
        RestTemplate restTemplate = new RestTemplate();
        String url = BOXOFFICE_LIST_API_URL + "?key=" + apiKey + "&targetDt=20240801"; // 날짜는 YYYYMMDD 형식으로 입력해야 함
        
		return restTemplate.getForObject(url, String.class);
	}

}
