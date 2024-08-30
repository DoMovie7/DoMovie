package com.red.domovie.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.movie.KmdbMovieResponse;
import com.red.domovie.service.MovieApiService;

@Service
public class MovieApiServiceProcess implements MovieApiService{

    @Value("${kobis.api.key}")
    private String kobisApiKey;
    
    @Value("${kmdb.api.key}")
    private String kmdbApiKey;
    

    private final String BOXOFFICE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private final String NEW_LIST_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";

	@Override
	public String getboxOffice() {
        RestTemplate restTemplate = new RestTemplate();
        String url = BOXOFFICE_LIST_API_URL + "?key=" + kobisApiKey + "&targetDt=20240829"; // 날짜는 YYYYMMDD 형식으로 입력해야 함
        
		return restTemplate.getForObject(url, String.class);
	}

	@Override
	public void getNewMovies(Model model) {
		LocalDate date = LocalDate.now().minusDays(1);
        
        // 원하는 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        
        // LocalDate 객체를 형식에 맞게 포맷
        String formattedDate = date.format(formatter);
		
	    RestTemplate restTemplate = new RestTemplate();
	    StringBuilder strBuilder=new StringBuilder(NEW_LIST_API_URL);
	    strBuilder.append("&detail=Y");
	    strBuilder.append("&releaseDts="+formattedDate);
	    strBuilder.append("&listCount=10");
	    strBuilder.append("&ServiceKey=" + kmdbApiKey);
	    
	    String url = strBuilder.toString();

	    try {
	        String response = restTemplate.getForObject(url, String.class);
	        JSONObject jsonResponse = new JSONObject(response);

	        if (jsonResponse.has("Data") && jsonResponse.getJSONArray("Data").length() > 0) {
	            JSONArray dataArray = jsonResponse.getJSONArray("Data");
	            //JSONArray firstData = dataArray.getJSONArray(0);
	            JSONObject firstData = dataArray.getJSONObject(0);
	            System.out.println(">>>>:"+firstData);
	            
	            ObjectMapper objectMapper=new ObjectMapper();
	            KmdbMovieResponse kmdbMovieResponse=objectMapper.readValue(firstData.toString(), new TypeReference<KmdbMovieResponse>() {
				});
	            
	            System.out.println("***:"+kmdbMovieResponse);
	            model.addAttribute("list", kmdbMovieResponse.getResult());
	        } else {
	            
	        }
	    } catch (JSONException e) {
	        e.printStackTrace();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	    }
	}

}
