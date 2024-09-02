package com.red.domovie.domain.repository.hometheater;

import com.red.domovie.domain.entity.hometheater.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}