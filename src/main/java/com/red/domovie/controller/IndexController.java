package com.red.domovie.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getSearchMovies(@RequestParam("keyword") String keyword, Model model) {
        // KMDB API를 호출하여 검색 결과를 가져옵니다.
        List<KmdbMovieDTO> movies = movieApiService.searchMovies(keyword);
        model.addAttribute("list", movies);
        model.addAttribute("keyword", keyword);  // 검색어를 모델에 추가하여 폼에 표시
        return "views/index/search-movie"; // Thymeleaf 템플릿 파일
    }

    
}
