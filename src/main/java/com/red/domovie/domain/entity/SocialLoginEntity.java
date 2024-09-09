package com.red.domovie.domain.entity;

import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "social")
public class SocialLoginEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String provider;  // "google", "naver", "kakao" 등
    private String providerId;
    private String nickName;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialLoginEntity that = (SocialLoginEntity) o;
        return Objects.equals(providerId, that.providerId) &&
               Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, provider);
    }

}
