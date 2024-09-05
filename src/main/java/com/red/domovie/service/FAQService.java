package com.red.domovie.service;

import java.util.List;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;

public interface FAQService {

	List<FAQDTO> getAllFAQs();

	FAQDTO addFAQ(FAQDTO faqDTO);

	FAQDTO updateFAQ(Long id, FAQDTO faqDTO);

	void deleteFAQ(Long id);

	void addFirstFAQ(String name);
}
