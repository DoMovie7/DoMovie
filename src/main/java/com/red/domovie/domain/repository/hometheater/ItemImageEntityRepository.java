package com.red.domovie.domain.repository.hometheater;

import com.red.domovie.domain.entity.hometheater.HomeTheaterEntity;
import com.red.domovie.domain.entity.hometheater.ItemImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemImageEntityRepository extends JpaRepository <ItemImageEntity, Long> {

    List<ItemImageEntity> findByHomeTheater(HomeTheaterEntity entity);

}
