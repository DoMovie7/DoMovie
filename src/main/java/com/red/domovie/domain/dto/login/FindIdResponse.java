package com.red.domovie.domain.dto.login;

import java.util.List;

public class FindIdResponse {
    private List<String> emails;

    public FindIdResponse(List<String> emails) {
        this.emails = emails;
    }

    // getters and setters
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}