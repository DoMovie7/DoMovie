package com.red.domovie.domain.dto.bot;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDTO {
	private String content; // 응답 메시지 내용
    private Set<String> nouns; // 명사 집합
    private Set<String> verbs; // 동사 집합 (추가된 필드)
}
