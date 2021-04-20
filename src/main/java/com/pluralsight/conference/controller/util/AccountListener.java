package com.pluralsight.conference.controller.util;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountListener implements ApplicationListener<OnCreateAccountUtilEvent> {

    private String serverUrl = "https://localhost:8080/";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountService accountService; // to create verification tokent

    @Override
    public void onApplicationEvent(OnCreateAccountUtilEvent event) {
        this.confirmCreateAccount(event);
    }

    private void confirmCreateAccount(OnCreateAccountUtilEvent event) {
        // get the account - comes out of event
        Account account = event.getAccount();
        String token = UUID.randomUUID().toString();
        // create  verification token
        // save to the database
        accountService.createVerificationToken(account, token);
        // get the email properties
        String recipientAddress = account.getEmail();
        String subject = "Account Confirmation";
        String confirmationUrl = "accountConfirm?token=" + token;
        String message = "Please confirm:";
        // send the email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
