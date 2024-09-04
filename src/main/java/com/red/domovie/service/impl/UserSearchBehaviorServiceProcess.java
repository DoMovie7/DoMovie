package com.red.domovie.service.impl;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.entity.UserSearchBehaviorEntity;
import com.red.domovie.domain.repository.UserSearchBehaviorRepository;
import com.red.domovie.service.UserSearchBehaviorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSearchBehaviorServiceProcess implements UserSearchBehaviorService{

	private final UserSearchBehaviorRepository behaviorRepository;
	
    @Override
    public void saveUserSearchBehavior(UserSearchBehaviorEntity behavior) {
        behaviorRepository.save(behavior);
    }
}
