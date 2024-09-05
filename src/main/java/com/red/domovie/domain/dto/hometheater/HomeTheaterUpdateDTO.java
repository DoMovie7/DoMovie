package com.red.domovie.domain.dto.hometheater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  // 이 줄을 추가합니다
public class HomeTheaterUpdateDTO {
    private String title;
    private String content;
}
