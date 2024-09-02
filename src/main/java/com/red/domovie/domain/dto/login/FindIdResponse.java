package com.red.domovie.domain.dto.login;

public class FindIdResponse {
    private String email;

    public FindIdResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}