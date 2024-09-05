package com.red.domovie.domain.entity;

import java.util.List;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chatroom")
public class ChatRoomEntity extends BaseEntity{
	
	@Id
	private String roomId;
	
	@Column(nullable = false)
	private long userId;
	
	@Column(nullable = true)
	private long adminId;

}
