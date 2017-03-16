package com.ilya40umov.badge.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents user's account.
 *
 * @author isorokoumov
 */
@Entity
@Table(name = "account",
        indexes = {@Index(name = "account_by_email", columnList = "email", unique = true)})
public class Account {

    private Long accountId;

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private Instant created;
    private Instant modified;

    private Set<AccountPrivilege> accountPrivileges = new LinkedHashSet<>();
    private Set<AccountBadge> accountBadges = new LinkedHashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", updatable = false, nullable = false)
    public Long getAccountId() {
        return accountId;
    }

    public Account setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    @Column(name = "email", length = 256, nullable = false)
    public String getEmail() {
        return email;
    }

    public Account setEmail(String email) {
        this.email = email;
        return this;
    }

    @Column(name = "password", length = 64, nullable = false)
    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(name = "first_name", length = 64, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public Account setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Column(name = "last_name", length = 64, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public Account setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @CreatedDate
    @Column(name = "created", columnDefinition = "datetime(3)", nullable = false, updatable = false)
    public Instant getCreated() {
        return created;
    }

    public Account setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @LastModifiedDate
    @Column(name = "modified", columnDefinition = "datetime(3)", nullable = false)
    public Instant getModified() {
        return modified;
    }

    public Account setModified(Instant modified) {
        this.modified = modified;
        return this;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true,
            cascade = CascadeType.ALL)
    public Set<AccountPrivilege> getAccountPrivileges() {
        return accountPrivileges;
    }

    public Account setAccountPrivileges(Set<AccountPrivilege> accountPrivileges) {
        this.accountPrivileges = accountPrivileges;
        return this;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true)
    public Set<AccountBadge> getAccountBadges() {
        return accountBadges;
    }

    public Account setAccountBadges(Set<AccountBadge> accountBadges) {
        this.accountBadges = accountBadges;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
