package com.red.domovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.red.domovie.domain.entity.SearchTrendEntity;
import com.red.domovie.domain.entity.UserSearchBehaviorEntity;
import com.red.domovie.service.SearchTrendService;
import com.red.domovie.service.UserSearchBehaviorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SearchController {

   
    private final UserSearchBehaviorService userSearchBehaviorService;

   
    private final SearchTrendService searchTrendService;

    // 사용자 검색 행동을 기록하는 API
    @PostMapping("/user/search")
    public void recordUserSearch(@RequestBody UserSearchBehaviorEntity behavior) {
    	System.out.println(behavior);
        userSearchBehaviorService.saveUserSearchBehavior(behavior);
    }

    // 검색 트렌드를 조회하는 API
    @GetMapping("/trends")
    public List<SearchTrendEntity> getSearchTrends() {
        return searchTrendService.getAllSearchTrends();
    }
}
