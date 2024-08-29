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

    public String getAllMovies() {
        RestTemplate restTemplate = new RestTemplate();
        String url = MOVIE_LIST_API_URL + "?key=" + apiKey;
        
        // API 호출 및 결과 반환
        return restTemplate.getForObject(url, String.class);
    }

}
