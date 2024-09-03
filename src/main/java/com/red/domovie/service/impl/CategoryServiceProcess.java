package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.entity.FAQEntity;
import com.red.domovie.domain.repository.FaqEntityRespository;
import com.red.domovie.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceProcess implements CategoryService{
	
	private final FaqEntityRespository faqRespository;
	
	@Override
	public List<FAQDTO> getAllFaqCategories() {
		
		List<FAQEntity> rootCategories = faqRespository.findByParentIsNull();
		
		return rootCategories.stream()
                .map(this::convertToFAQDTO)
                .collect(Collectors.toList());
	}
	
	private FAQDTO convertToFAQDTO(FAQEntity faqEntity) {
		FAQDTO dto = new FAQDTO();
        dto.setId(faqEntity.getId());
        dto.setName(faqEntity.getName());
        dto.setContent(faqEntity.getContent());
        
        dto.setChildren(faqEntity.getChildren().stream()
            .map(this::convertToFAQDTO)
            .collect(Collectors.toList()));
        return dto;
    }
	

}
