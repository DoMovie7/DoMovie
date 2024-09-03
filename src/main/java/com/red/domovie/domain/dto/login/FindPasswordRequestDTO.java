package com.red.domovie.domain.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequestDTO {
	private String userName;
    private String email;
}
