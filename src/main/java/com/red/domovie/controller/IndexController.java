package com.red.domovie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.red.domovie.domain.dto.movie.KmdbMovieDTO;
import com.red.domovie.service.MovieApiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final MovieApiService movieApiService;
    
    //비동기로 요청하면 html이 응답데이터
    @GetMapping("/movies/new")
    public String getNewMovies(Model model) {
        // 영화 정보 가져오기
        movieApiService.getNewMovies(model);
        
        return "views/index/index-movie-list";  // 타임리프 템플릿 이름 반환
    }
    
    
    @GetMapping("/movies/boxOffice")
    public String getBoxOffice(Model model) {
    
    	movieApiService.getBoxOffice(model);
        return "views/index/index-box-movie-list";
    }
    
    @GetMapping("/movies/horror")
    public String getHorrorMovies(Model model) {
      
    	movieApiService.getHorrorMovies(model);
        return "views/index/index-movie-list";
    }
    
    @GetMapping("/movies/animation")
    public String getAnimationMovies(Model model) {
      
    	movieApiService.getAnimationMovies(model);
        return "views/index/index-movie-list";
    }
    
    //getUpcomingMovies
    @GetMapping("/movies/upcoming")
    public String getUpcomingMovies(Model model) {
      
    	movieApiService.getUpcomingMovies(model);
        return "views/index/index-movie-list";
    }
    
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<List<KmdbMovieDTO>> getSearchMovies(@RequestParam("keyword") String keyword) {
        List<KmdbMovieDTO> movies = movieApiService.searchMovies(keyword);
        return ResponseEntity.ok(movies);
    }
    
    
}
