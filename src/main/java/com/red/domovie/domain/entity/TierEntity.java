package com.red.domovie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long tierId;
	@Column(nullable = true)
	private int minPostCount;
	@Column(nullable = true)
	private int maxPostCount;
}
