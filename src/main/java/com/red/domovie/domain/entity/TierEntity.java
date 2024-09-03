package com.red.domovie.domain.entity;

import com.red.domovie.domain.enums.Tier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tier")
public class TierEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tierId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tier tier;

    @Column(nullable = false)
    private int minPostCount;

    @Column(nullable = false)
    private int maxPostCount;

    @Column(nullable = false)
    private String description;

    // Tier enum으로부터 TierEntity를 생성하는 정적 메서드
    public static TierEntity fromTier(Tier tier) {
        return TierEntity.builder()
                .tier(tier)
                .minPostCount(tier.getMinPostCount())
                .maxPostCount(tier.getMaxPostCount())
                .description(tier.getDescription())
                .build();
    }
}