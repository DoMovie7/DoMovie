package com.red.domovie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.service.MovieApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final MovieApiService movieApiService;

    @GetMapping("/movies/all")
    public String getAllMovies() {
        // 모든 영화 정보를 서비스로부터 받아옴
        return movieApiService.getAllMovies();
    }
    
    @GetMapping("/movies/boxOffice")
    public String getboxOffice() {
        // 모든 영화 정보를 서비스로부터 받아옴
        return movieApiService.getboxOffice();
    }
    
    
}
