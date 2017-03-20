package com.ilya40umov.badge.dto.command;

import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;

import javax.validation.Valid;

/**
 * Contains data required to create an account.
 *
 * @author isorokoumov
 */
public class CreateAccount {

    @Valid
    private AccountDetails account;

    @Valid
    private PasswordDetails password;

    public AccountDetails getAccount() {
        return account;
    }

    public CreateAccount setAccount(AccountDetails account) {
        this.account = account;
        return this;
    }

    public PasswordDetails getPassword() {
        return password;
    }

    public CreateAccount setPassword(PasswordDetails password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "CreateAccount{" +
                "account=" + account +
                ", password=" + password +
                '}';
    }
}
