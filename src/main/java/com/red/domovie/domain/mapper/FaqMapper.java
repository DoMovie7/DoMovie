package com.red.domovie.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FaqMapper {

	void saveFirstFAQ(@Param("name") String name);

}
