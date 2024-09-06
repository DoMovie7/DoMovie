package com.red.domovie.domain.dto.hometheater;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class ItemImageSaveDTO {

    private String[] bucketKey;
    private String[] orgName;
    private String[] newName;
    private boolean[] isDefault;

    public List<ImageSaveDTO> toImageSaveListDTO(){
        // 배열 길이 검증
        if (bucketKey.length != orgName.length || bucketKey.length != newName.length || bucketKey.length != isDefault.length) {
            throw new IllegalStateException("Arrays must have the same length.");
        }

        // Stream을 사용하여 리스트 생성
        return IntStream.range(0, bucketKey.length)
                .mapToObj(i -> ImageSaveDTO.builder()
                        .bucketKey(bucketKey[i])
                        .orgName(orgName[i])
                        .newName(newName[i])
                        .isDefault(isDefault[i])
                        .build())
                .toList();
    }

}
