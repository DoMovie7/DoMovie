package com.red.domovie.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.red.domovie.domain.entity.FAQEntity;

public interface FaqEntityRespository extends JpaRepository<FAQEntity, Long>{

	List<FAQEntity> findByParentIsNull();

	List<FAQEntity> findAllByParentIsNull();

}
