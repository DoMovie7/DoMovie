package com.red.domovie.domain.dto.login;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class FindPasswordRequestDTO {
	private String userName;
    private String email;
}
