package com.ilya40umov.badge.util;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.Privilege;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * @author isorokoumov
 */
public class TestAccountBuilder {

    public static final String DEFAULT_EMAIL = "account@abc.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_FIRST_NAME = "John";
    public static final String DEFAULT_LAST_NAME = "Snow";

    private Long accountId;

    private String email = DEFAULT_EMAIL;
    private String password = DEFAULT_PASSWORD;
    private String firstName = DEFAULT_FIRST_NAME;
    private String lastName = DEFAULT_LAST_NAME;

    private Instant created;
    private Instant modified;

    private Set<AccountPrivilege> accountPrivileges = new LinkedHashSet<>();
    private Set<AccountBadge> accountBadges = new LinkedHashSet<>();

    public static TestAccountBuilder testAccountBuilder() {
        return new TestAccountBuilder();
    }

    public TestAccountBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public TestAccountBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public TestAccountBuilder withEmptyCreatedAndModified() {
        this.created = null;
        this.modified = null;
        return this;
    }

    public TestAccountBuilder withPrivileges(AccountPrivilege... privileges) {
        accountPrivileges.addAll(Arrays.asList(privileges));
        return this;
    }

    public TestAccountBuilder withAllPrivileges() {
        Arrays.stream(Privilege.values())
                .map(AccountPrivilege::fromPrivilege)
                .forEach(accountPrivileges::add);
        return this;
    }

    public TestAccountBuilder withCreateBadgePrivilege() {
        accountPrivileges.add(AccountPrivilege.fromPrivilege(Privilege.CREATE_BADGE));
        return this;
    }

    public TestAccountBuilder withBadges(AccountBadge... badges) {
        accountBadges.addAll(Arrays.asList(badges));
        return this;
    }

    public Account build() {
        checkNotNull(email);
        checkNotNull(password);
        checkNotNull(firstName);
        checkNotNull(lastName);
        Account account = new Account().setAccountId(accountId).setEmail(email)
                .setPassword(password).setFirstName(firstName).setLastName(lastName)
                .setCreated(created).setModified(modified);
        accountPrivileges.forEach(ap -> ap.setAccount(account));
        accountBadges.forEach(ab -> ab.setAccount(account));
        return account
                .setAccountPrivileges(accountPrivileges)
                .setAccountBadges(accountBadges);
    }

}
