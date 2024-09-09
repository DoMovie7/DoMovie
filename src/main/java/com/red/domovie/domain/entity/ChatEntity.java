package com.red.domovie.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class ChatEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long chatId;
	
	@ManyToOne
	@JoinColumn(name="roomId", nullable = false)
	private ChatRoomEntity room;
	
	@Column(nullable = true)
	private String message;
	
	@Column(nullable = true)
	@ColumnDefault("1")
	private int isUser;//0:admin, 1:user
	
	@CreationTimestamp
	@Column(columnDefinition = "timestamp")
	protected LocalDateTime createdAt;
	
}

