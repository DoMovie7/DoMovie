package com.red.domovie.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
	

	@CreationTimestamp
	@Column(columnDefinition = "timestamp")
	private LocalDateTime createdAt;
	
	

	@UpdateTimestamp
	@Column(columnDefinition = "timestamp")
	private LocalDateTime updatedAt;

}
