package com.red.domovie.domain.dto.chat;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO {
	
	private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<ChattingRoomDTO> content;
    
    public PageDTO(int page, int size, long totalElements, List<ChattingRoomDTO> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.content = content;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }

}
