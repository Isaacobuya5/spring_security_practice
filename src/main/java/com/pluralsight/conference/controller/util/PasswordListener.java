package com.pluralsight.conference.controller.util;

import com.pluralsight.conference.model.Password;
import com.pluralsight.conference.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordListener implements ApplicationListener<OnPasswordResetEvent> {

    private String serverUrl = "http://localhost:8080/"; // could be injected

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordService passwordService;

    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        this.resetPassword(event);
    }

    private void resetPassword(OnPasswordResetEvent event) {
        // create the password token
        Password password = event.getPassword();
        String token = UUID.randomUUID().toString();
        passwordService.createResetToken(password, token);
        // get email properties
        String recipientAddress = password.getEmail();
        String subject = "Reset Password";
        String confirmationUrl = "passwordReset?token=" + token;
        String message = "Reset Password:";
        // send the actual email using mail sender
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
