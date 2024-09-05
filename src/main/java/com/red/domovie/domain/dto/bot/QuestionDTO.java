package com.red.domovie.domain.dto.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
	
    private String key; //질문자를 구분하기위한 값
    private String content; //질문내용
    private long userId; //guest : 0
    
}
