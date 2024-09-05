package com.red.domovie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.entity.FAQEntity;
import com.red.domovie.domain.mapper.FaqMapper;
import com.red.domovie.domain.repository.FaqEntityRespository;
import com.red.domovie.service.FAQService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FAQServiceProcess implements FAQService{
	
	private final FaqEntityRespository faqRepository;
	private final FaqMapper faqMapper;
	
	@Override
	public List<FAQDTO> getAllFAQs() {
		List<FAQEntity> rootFAQs = faqRepository.findAllByParentIsNull();
        return rootFAQs.stream()
                .map(FAQDTO::fromEntity)
                .collect(Collectors.toList());
	}
	
	@Override
	public FAQDTO addFAQ(FAQDTO faqDTO) {
		
        FAQEntity faq = new FAQEntity();
        faq.setName(faqDTO.getName());
        faq.setContent(faqDTO.getContent());
        if (faqDTO.getChildren().get(0).getId() != null) {
            faq.setParent(faqRepository.findById(faqDTO.getChildren().get(0).getId()).orElse(null));
        }
        FAQEntity savedFAQ = faqRepository.save(faq);
        return FAQDTO.fromEntity(savedFAQ);
    }

	@Override
    public FAQDTO updateFAQ(Long id, FAQDTO faqDTO) {
        FAQEntity faq = faqRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("FAQ not found"));
        faq.setName(faqDTO.getName());
        faq.setContent(faqDTO.getContent());
        FAQEntity updatedFAQ = faqRepository.save(faq);
        return FAQDTO.fromEntity(updatedFAQ);
    }

	@Override
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }

	@Override
	public void addFirstFAQ(String name) {
		faqMapper.saveFirstFAQ(name);
		
	}

}
