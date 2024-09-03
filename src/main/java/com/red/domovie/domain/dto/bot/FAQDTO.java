package com.red.domovie.domain.dto.bot;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FAQDTO {
	private Long id;
    private String name;
    private String content;
    private List<FAQDTO> children;
}
