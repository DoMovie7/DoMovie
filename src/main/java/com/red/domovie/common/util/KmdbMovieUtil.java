package com.red.domovie.common.util;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class KmdbMovieUtil {
	
	
    @Value("${kmdb.api.key}")
    private String kmdbApiKey;
    
    private String kmdbEndPoint ="http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y";
    
    private String movieId;
    
    private String movieSeq;
    
    
    
    
    
    private final RestTemplate restTemplate;
    
    
    //요청 메서드
    
    public ResponseEntity<JsonNode> getDtailMovie(String movieID) {
    	
        // 영문자만 추출하여 movieId에 할당
        movieId = movieID.replaceAll("[^a-zA-Z]", "");
        
        // 숫자만 추출하여 movieSeq에 할당
        movieSeq = movieID.replaceAll("[^0-9]", "");
    	
        HttpHeaders headers = new HttpHeaders();//http 요청 헤더 설정을 위한 객체 
        //Authorization"은 HTTP 표준 헤더,"Bearer"는 토큰 기반 인증의 한 유형,억세스토큰
        HttpEntity<String> entity = new HttpEntity<>(headers);//HttpEntity는 http요청과 응답을 나타내는 클래스,RestTemplate의 메서드들(예: exchange())은 이 객체를 파라미터로 받기를 요구함)
        
        //실제 요청할 url주소,요청 메서드, 포함시킬 헤더 정보,요청 후 응답받은 json을 JsonNode 형태로 받기 위함
        String url =kmdbEndPoint+"&movieId="+movieId+"&movieSeq="+movieSeq+"&ServiceKey="+kmdbApiKey;
        System.out.println(url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // JsonNode를 포함하는 ResponseEntity를 반환
        return new ResponseEntity<>(jsonNode, response.getStatusCode());
    }
    
    
    }
    
    
    


