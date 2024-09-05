package com.red.domovie.domain.dto.bot;

import java.util.List;
import java.util.stream.Collectors;

import com.red.domovie.domain.entity.FAQEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQDTO {
	private Long id;
    private String name;
    private String content;
    private List<FAQDTO> children;
    
    public static FAQDTO fromEntity(FAQEntity entity) {
        FAQDTO dto = new FAQDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setContent(entity.getContent());
        dto.setChildren(entity.getChildren().stream()
                .map(FAQDTO::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }
}
