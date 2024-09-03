package com.red.domovie.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceProcess {

	@Autowired
	private final JavaMailSender emailSender;

	public void sendPasswordResetEmail(String to, String resetToken) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply@yourdomain.com");
		message.setTo(to);
		message.setSubject("비밀번호 재설정");
		message.setText("비밀번호를 재설정하려면 다음 링크를 클릭하세요: " + "http://yourdomain.com/reset-password?token=" + resetToken);
		emailSender.send(message);
	}
}
