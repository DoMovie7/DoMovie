package com.red.domovie.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.movie.BoxOfficeDTO;
import com.red.domovie.domain.dto.movie.KmdbMovieDTO;
import com.red.domovie.domain.dto.movie.KmdbMovieResponse;
import com.red.domovie.service.MovieApiService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovieApiServiceProcess implements MovieApiService {

    @Value("${kobis.api.key}")
    private String kobisApiKey;
    
    @Value("${kmdb.api.key}")
    private String kmdbApiKey;
    
    private final String BOXOFFICE_LIST_API_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    //private final String MOVIE_LIST_API_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    private final String KMDB_LIST_API_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    @Override
    @Cacheable(value = "boxOfficeCache", key = "#root.method.name")
    public List<BoxOfficeDTO> getBoxOffice() {
       // log.info("getBoxOffice 메서드 호출 - 캐시되지 않은 경우에만 이 로그가 표시됩니다.");

        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlBuilder = new StringBuilder(BOXOFFICE_LIST_API_URL);
        urlBuilder.append("?key=").append(kobisApiKey);
        urlBuilder.append("&targetDt=").append(formattedDate);

        String url = urlBuilder.toString();
        List<BoxOfficeDTO> boxOfficeDTOs = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode boxOfficeResult = jsonResponse.get("boxOfficeResult");
            JsonNode dailyBoxOfficeList = boxOfficeResult.get("dailyBoxOfficeList");

            for (JsonNode movie : dailyBoxOfficeList) {
                BoxOfficeDTO boxOfficeDTO = objectMapper.treeToValue(movie, BoxOfficeDTO.class);
                
                Map<String, String> posterData = fetchPosterFromKMDB(boxOfficeDTO.getMovieNm(), boxOfficeDTO.getOpenDt());
                boxOfficeDTO.setPosters(posterData.get("poster"));
                boxOfficeDTO.setDOCID(posterData.get("DOCID"));
                
                boxOfficeDTOs.add(boxOfficeDTO);
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }
        return boxOfficeDTOs;
    }

    private Map<String, String> fetchPosterFromKMDB(String movieNm, String releaseDts) {
        final String DEFAULT_POSTER = "/img/index/no-movie-img.jpg";
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&title=").append(movieNm);
        
        if (releaseDts != null && !releaseDts.isEmpty()) {
            strBuilder.append("&releaseDts=").append(releaseDts);
        }

        strBuilder.append("&listCount=1");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        String url = strBuilder.toString();

        Map<String, String> result = new HashMap<>();
        result.put("poster", DEFAULT_POSTER);
        result.put("DOCID", "");
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);

                if (firstData.has("Result") && firstData.get("Result").size() > 0) {
                    JsonNode movieData = firstData.get("Result").get(0);

                    String poster = movieData.get("posters").asText("").split("[|]")[0];
                    String docid = movieData.get("DOCID").asText("");

                    result.put("poster", poster.isEmpty() ? DEFAULT_POSTER : poster);
                    result.put("DOCID", docid);
                } else {
                    log.warn("Result array is missing or empty in the JSON response.");
                }
            } else {
                log.warn("Data array is missing or empty in the JSON response.");
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }

        return result;
    }

    @Override
    @Cacheable(value = "newMoviesCache", key = "#root.method.name")
    public List<KmdbMovieDTO> getNewMovies() {
        LocalDate currentDate = LocalDate.now();
        LocalDate threeMonthsAgo = currentDate.minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDate = threeMonthsAgo.format(formatter);
        String endDate = currentDate.format(formatter);

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&releaseDts=").append(startDate);
        strBuilder.append("&releaseDte=").append(endDate);
        strBuilder.append("&listCount=100");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");

        String url = strBuilder.toString();
        List<KmdbMovieDTO> filteredAndSortedMovies = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);
                KmdbMovieResponse kmdbMovieResponse = objectMapper.treeToValue(firstData, KmdbMovieResponse.class);
                List<KmdbMovieDTO> allMovies = kmdbMovieResponse.getResult();

                filteredAndSortedMovies = allMovies.stream()
                    .filter(movie -> {
                        String repRlsDate = movie.getRepRlsDate();
                        if (repRlsDate == null || repRlsDate.equals("Unknown") || repRlsDate.length() != 8) {
                            return false;
                        }
                        try {
                            LocalDate releaseDate = LocalDate.parse(repRlsDate, formatter);
                            return !releaseDate.isAfter(currentDate) && !releaseDate.isBefore(threeMonthsAgo);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .sorted((m1, m2) -> m2.getRepRlsDate().compareTo(m1.getRepRlsDate()))
                    .limit(10)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }
        return filteredAndSortedMovies;
    }


    @Override
    @Cacheable(value = "upcomingMoviesCache", key = "#root.method.name")
    public List<KmdbMovieDTO> getUpcomingMovies() {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=Y");
        strBuilder.append("&releaseDts=").append(formattedDate);
        strBuilder.append("&listCount=10");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&use=극장용");
        
        String url = strBuilder.toString();
        List<KmdbMovieDTO> upcomingMoviesList = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);
                KmdbMovieResponse kmdbMovieResponse = objectMapper.treeToValue(firstData, KmdbMovieResponse.class);
                upcomingMoviesList = kmdbMovieResponse.getResult();
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }
        return upcomingMoviesList;
    }


    @Override
    public void getHorrorMovies(Model model) {
        LocalDate currentDate = LocalDate.now();
        LocalDate sixMonthsAgo = currentDate.minusMonths(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDate = sixMonthsAgo.format(formatter);
        String endDate = currentDate.format(formatter);

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=y");
        strBuilder.append("&genre=공포");
        strBuilder.append("&releaseDts=").append(startDate);
        strBuilder.append("&releaseDte=").append(endDate);
        strBuilder.append("&listCount=100");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");

        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);

                KmdbMovieResponse kmdbMovieResponse = objectMapper.treeToValue(firstData, KmdbMovieResponse.class);
                List<KmdbMovieDTO> allMovies = kmdbMovieResponse.getResult();

                List<KmdbMovieDTO> filteredAndSortedMovies = allMovies.stream()
                    .filter(movie -> {
                        String repRlsDate = movie.getRepRlsDate();
                        if (repRlsDate == null || repRlsDate.equals("Unknown") || repRlsDate.length() != 8) {
                            return false;
                        }
                        try {
                            LocalDate releaseDate = LocalDate.parse(repRlsDate, formatter);
                            return !releaseDate.isAfter(currentDate) && !releaseDate.isBefore(sixMonthsAgo);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .sorted((m1, m2) -> m2.getRepRlsDate().compareTo(m1.getRepRlsDate()))
                    .limit(10)
                    .collect(Collectors.toList());

                model.addAttribute("list", filteredAndSortedMovies);
            } else {
                model.addAttribute("list", new ArrayList<KmdbMovieDTO>());
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
            model.addAttribute("error", "데이터 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void getAnimationMovies(Model model) {
        LocalDate currentDate = LocalDate.now();
        LocalDate sixMonthsAgo = currentDate.minusMonths(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startDate = sixMonthsAgo.format(formatter);
        String endDate = currentDate.format(formatter);

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=y");
        strBuilder.append("&type=애니메이션");
        strBuilder.append("&releaseDts=").append(startDate);
        strBuilder.append("&releaseDte=").append(endDate);
        strBuilder.append("&listCount=100");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");
        strBuilder.append("&use=극장용");

        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);

                KmdbMovieResponse kmdbMovieResponse = objectMapper.treeToValue(firstData, KmdbMovieResponse.class);
                List<KmdbMovieDTO> allMovies = kmdbMovieResponse.getResult();

                List<KmdbMovieDTO> filteredAndSortedMovies = allMovies.stream()
                    .filter(movie -> {
                        String repRlsDate = movie.getRepRlsDate();
                        if (repRlsDate == null || repRlsDate.equals("Unknown") || repRlsDate.length() != 8) {
                            return false;
                        }
                        try {
                            LocalDate releaseDate = LocalDate.parse(repRlsDate, formatter);
                            return !releaseDate.isAfter(currentDate) && !releaseDate.isBefore(sixMonthsAgo);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .sorted((m1, m2) -> m2.getRepRlsDate().compareTo(m1.getRepRlsDate()))
                    .limit(10)
                    .collect(Collectors.toList());

                model.addAttribute("list", filteredAndSortedMovies);
            } else {
                model.addAttribute("list", new ArrayList<KmdbMovieDTO>());
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
            model.addAttribute("error", "데이터 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    public List<KmdbMovieDTO> searchMovies(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=y");
        strBuilder.append("&title=").append(keyword);
        strBuilder.append("&listCount=5");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");

        String url = strBuilder.toString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("API Response: {}", response);

            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data") && jsonResponse.get("Data").size() > 0) {
                JsonNode firstData = jsonResponse.get("Data").get(0);

                KmdbMovieResponse kmdbMovieResponse = objectMapper.treeToValue(firstData, KmdbMovieResponse.class);

                return kmdbMovieResponse.getResult();
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }

        log.warn("검색 결과가 없거나 오류가 발생했습니다.");
        return new ArrayList<>();
    }

    @Override
    public List<KmdbMovieDTO> getAutoCompleteSuggestions(String query) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder strBuilder = new StringBuilder(KMDB_LIST_API_URL);
        strBuilder.append("&detail=y");
        strBuilder.append("&query=").append(query);
        strBuilder.append("&listCount=5");
        strBuilder.append("&ServiceKey=").append(kmdbApiKey);
        strBuilder.append("&sort=prodYear,1");

        String url = strBuilder.toString();
        String response = restTemplate.getForObject(url, String.class);
        log.info("API Response: {}", response);

        List<KmdbMovieDTO> movies = new ArrayList<>();

        try {
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("Data")) {
                JsonNode dataArray = jsonResponse.get("Data");
                if (dataArray.size() > 0) {
                    JsonNode dataObject = dataArray.get(0);

                    if (dataObject.has("Result")) {
                        JsonNode resultArray = dataObject.get("Result");
                        int addedCount = 0;

                        for (JsonNode resultItem : resultArray) {
                            KmdbMovieDTO movie = new KmdbMovieDTO();
                            movie.setTitle(resultItem.get("title").asText());
                            movie.setGenre(resultItem.get("genre").asText());

                            if (!movie.getGenre().contains("에로") && !movie.getGenre().contains("드라마")) {
                                movies.add(movie);
                                addedCount++;
                            }

                            if (addedCount >= 5) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage());
        }

        return movies;
    }






}
