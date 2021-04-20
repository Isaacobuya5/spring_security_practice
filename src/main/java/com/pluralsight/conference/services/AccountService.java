package com.pluralsight.conference.services;

import com.pluralsight.conference.model.Account;

public interface AccountService {

    public Account create(Account account);

    void createVerificationToken(Account account, String token);

    void confirmAccount(String token);
}
