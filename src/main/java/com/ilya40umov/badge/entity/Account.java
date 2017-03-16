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

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Column(name = "email", length = 256, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", length = 64, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "first_name", length = 64, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 64, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @CreatedDate
    @Column(name = "created", columnDefinition = "datetime(3)", nullable = false, updatable = false)
    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @LastModifiedDate
    @Column(name = "modified", columnDefinition = "datetime(3)", nullable = false)
    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true,
            cascade = CascadeType.ALL)
    public Set<AccountPrivilege> getAccountPrivileges() {
        return accountPrivileges;
    }

    public void setAccountPrivileges(Set<AccountPrivilege> accountPrivileges) {
        this.accountPrivileges = accountPrivileges;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true)
    public Set<AccountBadge> getAccountBadges() {
        return accountBadges;
    }

    public void setAccountBadges(Set<AccountBadge> accountBadges) {
        this.accountBadges = accountBadges;
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
