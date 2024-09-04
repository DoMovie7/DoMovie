package com.red.domovie.service;

public interface EmailService {

	void sendPasswordResetEmail(String to, String resetToken);

}
