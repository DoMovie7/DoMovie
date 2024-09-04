package com.red.domovie.service.impl;

import java.time.LocalDateTime;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.red.domovie.domain.entity.UserEntity;
import com.red.domovie.domain.mapper.LoginMapper;
import com.red.domovie.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceProcess implements EmailService {

    private final JavaMailSender emailSender;
    private final LoginMapper loginMapper;

    @Value("${app.resetPasswordUrl}")
    private String resetPasswordUrl;

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        try {
            // 이메일 발송 전에 DB 업데이트 시도
            UserEntity user = loginMapper.findByEmail(to);
            if (user != null) {
                user.setPasswordResetToken(resetToken);
                int updatedRows = loginMapper.updateUser(user);
                log.info("Updated user rows: {}", updatedRows);
                if (updatedRows == 0) {
                    log.error("Failed to update user with reset token. Email: {}", to);
                }
            } else {
                log.error("User not found for email: {}", to);
                return; // 사용자를 찾지 못했으면 이메일을 보내지 않고 종료
            }

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            
            String htmlMsg = String.format(
                "<h3>비밀번호 재설정</h3>" +
                "<p>비밀번호를 재설정하려면 다음 링크를 클릭하세요:</p>" +
                "<p><a href='%s?token=%s'>비밀번호 재설정</a></p>" +
                "<p>이 링크는 24시간 동안 유효합니다.</p>",
                resetPasswordUrl, resetToken
            );

            helper.setTo(to);
            helper.setSubject("비밀번호 재설정");
            helper.setText(htmlMsg, true); // true를 설정하여 HTML 사용

            emailSender.send(mimeMessage);
            log.info("Password reset email sent to: {}", to);
        } catch (Exception e) {
            log.error("Error sending password reset email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
