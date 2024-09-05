package com.red.domovie.service;

import java.util.List;

import org.springframework.ui.Model;

import com.red.domovie.domain.dto.movie.KmdbMovieDTO;



public interface MovieApiService {

	void getBoxOffice(Model model);

	void getNewMovies(Model model);

	void getHorrorMovies(Model model);

	void getAnimationMovies(Model model);

	void getUpcomingMovies(Model model);

	List<KmdbMovieDTO> searchMovies(String keyword);

}
