package com.red.domovie.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 
 * 사용법
 * 
 * @Autowired
 * private ModelMapper modelMapper;
 * 
 * // Entity to DTO
 * UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
 * 
 * // DTO to Entity
 * UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
 * 
 */
@Configuration
public class ModelMapperConfig {
	
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper=new ModelMapper();
		// ModelMapper의 설정을 구성
	    modelMapper.getConfiguration()
	        // 필드 간 매핑을 활성화 (Getter/Setter가 아닌 필드에 직접 접근하여 매핑)
	        .setFieldMatchingEnabled(true)
	        // 필드 접근 수준을 PRIVATE으로 설정 (private 필드에도 접근할 수 있게 함)
	        .setFieldAccessLevel(AccessLevel.PRIVATE)
	        // 매핑 전략을 느슨한 매칭으로 설정 (필드 이름이 정확히 일치하지 않더라도 유사한 이름의 필드를 매핑)
	        //.setMatchingStrategy(MatchingStrategies.LOOSE)
	        ;
		
		return modelMapper;
	}

}
