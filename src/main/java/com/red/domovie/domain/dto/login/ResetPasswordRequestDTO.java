package com.red.domovie.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordRequestDTO {
	private final boolean isValid;
    private final String errorMessage;
}
