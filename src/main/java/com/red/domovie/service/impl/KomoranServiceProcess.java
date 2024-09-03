package com.red.domovie.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.dto.bot.MessageDTO;
import com.red.domovie.service.KomoranService;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KomoranServiceProcess implements KomoranService{
	
	private final Komoran komoran; // Komoran 형태소 분석기

    public MessageDTO nlpAnalyze(String message) {
    	
        // Komoran을 사용하여 메시지 분석
        KomoranResult result = komoran.analyze(message);
        result.getTokenList().forEach(token -> {
            System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(),
                    token.getMorph(), token.getPos());
        });

        // 메시지에서 명사들을 추출한 후 중복 제거해서 Set에 저장
        Set<String> nouns = result.getNouns().stream()
                .collect(Collectors.toSet());
        nouns.forEach(noun -> {
            System.out.println("명사: " + noun);
        });

        // 메시지에서 동사들을 추출한 후 중복 제거해서 Set에 저장
        Set<String> verbs = result.getTokenList().stream()
                .filter(token -> "VV".equals(token.getPos())) // VV는 동사
                .map(token -> token.getMorph())
                .collect(Collectors.toSet());
        verbs.forEach(verb -> {
            System.out.println("동사: " + verb);
        });

        // 명사 및 동사 집합을 기반으로 응답 메시지 생성
        return analyzeToken(nouns, verbs);
    }

    private MessageDTO analyzeToken(Set<String> nouns, Set<String> verbs) {
    	
    	// 분석된 명사와 동사를 기반으로 응답 메시지를 생성하는 로직
    	String analyzedContent = "다음과 같은 키워드를 분석했습니다: " 
    	    + "명사 - " + String.join(", ", nouns) 
    	    + "; 동사 - " + String.join(", ", verbs);
    	
        // 메시지와 명사, 동사를 반환하는 메시지 DTO 생성
        MessageDTO messageDTO = MessageDTO.builder()
                .content(analyzedContent)
                .nouns(nouns)
                .verbs(verbs) // 동사 집합 추가
                .build();

        // 추가적인 로직이 필요하다면 여기에 추가
        return messageDTO;
    }

}
