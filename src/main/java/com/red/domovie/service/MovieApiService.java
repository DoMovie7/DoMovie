package com.red.domovie.service;

import java.util.List;

import org.springframework.ui.Model;

import com.red.domovie.domain.dto.movie.BoxOfficeDTO;
import com.red.domovie.domain.dto.movie.KmdbMovieDTO;



public interface MovieApiService {
	
    List<BoxOfficeDTO> getBoxOffice();  // Model 파라미터 제거
    List<KmdbMovieDTO> getNewMovies();  // 동일하게 다른 메서드들도 수정
    List<KmdbMovieDTO> getUpcomingMovies(); // 동일하게 수정


	//void getBoxOffice(Model model);

	//void getNewMovies(Model model);

	void getHorrorMovies(Model model);

	void getAnimationMovies(Model model);

	//void getUpcomingMovies(Model model);

	List<KmdbMovieDTO> searchMovies(String keyword);

	List<KmdbMovieDTO> getAutoCompleteSuggestions(String query);




}
