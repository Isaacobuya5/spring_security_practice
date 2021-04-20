package com.pluralsight.conference.controller;

import com.pluralsight.conference.controller.util.OnCreateAccountUtilEvent;
import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher; // suplied by spring to fire off events


    @GetMapping("account")
    public String getRegistration(@ModelAttribute("account") Account account) {
        return "account";
    }

    @PostMapping("account")
    public String addRegistration(@Valid @ModelAttribute ("account") Account account, BindingResult result) {
        // check for errors in Binding result
        // check for account to see if existing already
        // verify email address is valid

        // encrypt the new password coming in
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // create the new account
        account = accountService.create(account);
        // fire off an event for account creation
        eventPublisher.publishEvent(new OnCreateAccountUtilEvent(account, "conference_war"));
        return "redirect:account";
    }

    @GetMapping("accountConfirm")
    public String confirmAccount(@RequestParam("token") String token) {
        accountService.confirmAccount(token);
        return "accountConfirmed";
    };

}
