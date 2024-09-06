package com.red.domovie.domain.dto.hometheater;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImageSaveDTO {

    private String bucketKey;
    private String orgName;
    private String newName;
    private boolean isDefault;

}
