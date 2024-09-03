package com.red.domovie.service;

import org.springframework.ui.Model;

public interface MovieApiService {

	void getBoxOffice(Model model);

	void getNewMovies(Model model);

	void getHorrorMovies(Model model);

	void getAnimationMovies(Model model);



}
