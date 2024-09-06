package com.red.domovie.domain.entity.hometheater;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itemImage")
public class ItemImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;
    private String imageUrl;
    private String bucketKey;
    private String newName;
    private String orgName;
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "homeTheaterId")
    private HomeTheaterEntity homeTheater;

}
