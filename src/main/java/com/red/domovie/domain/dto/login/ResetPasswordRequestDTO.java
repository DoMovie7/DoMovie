package com.red.domovie.domain.dto.login;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDTO {
	private String resetToken;
    private String newPassword;
}
