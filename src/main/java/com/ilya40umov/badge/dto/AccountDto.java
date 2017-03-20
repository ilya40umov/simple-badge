package com.ilya40umov.badge.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.time.Instant;
import java.util.List;

/**
 * DTO that corresponds to {@link com.ilya40umov.badge.entity.Account} entity.
 *
 * @author isorokoumov
 */
public class AccountDto {

    private Long accountId;
    private String email;
    private String firstName;
    private String lastName;
    private Instant created;
    private Instant modified;
    private Boolean canCreateBadges;
    private List<AccountBadgeDto> accountBadges;

    @JsonView(Views.Public.class)
    public Long getAccountId() {
        return accountId;
    }

    public AccountDto setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    @JsonView(Views.Private.class)
    public String getEmail() {
        return email;
    }

    public AccountDto setEmail(String email) {
        this.email = email;
        return this;
    }

    @JsonView(Views.Public.class)
    public String getFirstName() {
        return firstName;
    }

    public AccountDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @JsonView(Views.Public.class)
    public String getLastName() {
        return lastName;
    }

    public AccountDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @JsonView(Views.Private.class)
    public Instant getCreated() {
        return created;
    }

    public AccountDto setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @JsonView(Views.Private.class)
    public Instant getModified() {
        return modified;
    }

    public AccountDto setModified(Instant modified) {
        this.modified = modified;
        return this;
    }

    @JsonView(Views.Private.class)
    public Boolean getCanCreateBadges() {
        return canCreateBadges;
    }

    public AccountDto setCanCreateBadges(Boolean canCreateBadges) {
        this.canCreateBadges = canCreateBadges;
        return this;
    }

    @JsonView(Views.Public.class)
    public List<AccountBadgeDto> getAccountBadges() {
        return accountBadges;
    }

    public AccountDto setAccountBadges(List<AccountBadgeDto> accountBadges) {
        this.accountBadges = accountBadges;
        return this;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", canCreateBadges=" + canCreateBadges +
                ", accountBadges=" + accountBadges +
                '}';
    }
}
