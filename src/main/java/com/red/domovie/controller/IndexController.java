package com.red.domovie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.red.domovie.domain.dto.movie.BoxOfficeDTO;
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
        // 서비스를 통해 새로운 영화 데이터를 가져옵니다.
        List<KmdbMovieDTO> newMoviesList = movieApiService.getNewMovies();
        
        // 가져온 데이터를 모델에 추가하여 뷰에서 사용할 수 있도록 합니다.
        model.addAttribute("list", newMoviesList);
        
        // 타임리프 템플릿 이름 반환
        return "views/index/index-movie-list";
    }
    
    @GetMapping("/movies/boxOffice")
    public String getBoxOffice(Model model) {
        // 서비스 메서드에서 데이터를 가져와서 리스트에 담습니다.
        List<BoxOfficeDTO> boxOfficeList = movieApiService.getBoxOffice();
        
        // 모델에 데이터를 추가하여 뷰에서 사용할 수 있게 합니다.
        model.addAttribute("list", boxOfficeList);
        
        // 렌더링할 뷰의 이름을 반환합니다.
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
        // 서비스를 통해 개봉 예정 영화 데이터를 가져옵니다.
        List<KmdbMovieDTO> upcomingMoviesList = movieApiService.getUpcomingMovies();
        
        // 가져온 데이터를 모델에 추가하여 뷰에서 사용할 수 있도록 합니다.
        model.addAttribute("list", upcomingMoviesList);
        
        // 타임리프 템플릿 이름 반환
        return "views/index/index-movie-list";
    }
    
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<List<KmdbMovieDTO>> getSearchMovies(@RequestParam("keyword") String keyword) {
        List<KmdbMovieDTO> movies = movieApiService.searchMovies(keyword);
        return ResponseEntity.ok(movies);
    }
    
}
