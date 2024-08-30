package com.red.domovie.service;

import org.springframework.ui.Model;

public interface MovieApiService {

	String getboxOffice();

	void getNewMovies(Model model);

}
