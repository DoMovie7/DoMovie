package com.red.domovie.domain.dto.hometheater;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ImageDetailDTO {

    private long imageId;
    private String imageUrl;
    private String bucketKey;
    private String orgName;
    private String newName;
    private boolean isDefault;

}
