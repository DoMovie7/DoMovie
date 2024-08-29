package com.red.domovie.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.red.domovie.domain.dto.recommend.RecommendListDTO;
import com.red.domovie.service.RecommendService;

//@Component 서비스는 빈으로 스프링컨테이너에 보관 객체를만들어서, 싱글톤으로 관리.
//매번 서비스를 실행할때마다 new RecommendServiceProcess() 만드면 처리속도 저하
@Service
public class RecommendServiceProcess implements RecommendService {

    // RecommendService 인터페이스를 구현한 listProcess 메서드
    @Override
    public void listProcess(Model model) {

        // 샘플 이미지 URL 배열 - 실제 구현에서는 데이터베이스나 API에서 가져온 데이터를 사용
        String[] sampleImages = {
                "",
                "https://search.pstatic.net/common?type=o&size=176x264&quality=85&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240823_282%2F17243737818666Gcav_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240814_274%2F1723626467288x02BG_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240731_171%2F1722418168362V2OKt_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240816_267%2F1723785813255Qbf1b_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240814_219%2F1723622988115akgs8_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240801_165%2F1722502693425nRHqQ_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240702_233%2F1719904004038eLqJl_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2",
                "https://search.pstatic.net/common?type=o&size=304x456&quality=100&direct=true&src=https%3A%2F%2Fs.pstatic.net%2Fmovie.phinf%2F20240711_161%2F1720689397463SLgkx_JPEG%2Fmovie_image.jpg%3Ftype%3Dw640_2"
        };

        // RecommendListDTO 객체를 담을 리스트 생성
        List<RecommendListDTO> list = new ArrayList<>();

        // 샘플 이미지 배열의 1번 인덱스부터 반복하여 RecommendListDTO 객체를 생성하고 리스트에 추가
        for (int i = 1; i < sampleImages.length; i++) {
            list.add(RecommendListDTO.builder()
                    .no(i)  // 항목 번호
                    .title("제목" + i)  // 항목 제목
                    .user("글쓴이" + i)  // 글쓴이 이름
                    .createdAt(LocalDateTime.now())  // 생성 시간
                    .imgUrl(sampleImages[i])  // 이미지 URL
                    .commentCount(i)
                    .build());  // RecommendListDTO 객체 생성 및 리스트에 추가
        }

        // 모델에 "list"라는 이름으로 생성한 리스트를 추가-> view(thymeleaf - html)에서 "${list}" 접근가능
        model.addAttribute("list", list);
    }
}

