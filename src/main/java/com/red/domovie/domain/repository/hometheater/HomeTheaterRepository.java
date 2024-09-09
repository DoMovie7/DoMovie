package com.red.domovie.domain.repository.hometheater;

import com.red.domovie.domain.entity.hometheater.HomeTheaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface HomeTheaterRepository extends JpaRepository<HomeTheaterEntity, Long> {
    List<HomeTheaterEntity> findByOrderByIdDesc();
}