package com.red.domovie.service;

import org.springframework.ui.Model;

public interface MovieDetailService {

	void findMovieDetail(String movieID, Model model);

}
