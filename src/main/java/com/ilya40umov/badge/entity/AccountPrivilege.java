package com.ilya40umov.badge.entity;

import javax.persistence.*;

/**
 * Represents a single privilege/role granted to a user.
 *
 * @author isorokoumov
 */
@Entity
@Table(name = "account_privilege")
public class AccountPrivilege {

    private AccountPrivilegeId accountPrivilegeId;
    private Account account;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "privilege",
                    column = @Column(name = "privilege_id", nullable = false)),
            @AttributeOverride(name = "accountId",
                    column = @Column(name = "account_id", nullable = false))

    })
    public AccountPrivilegeId getAccountPrivilegeId() {
        return accountPrivilegeId;
    }

    public void setAccountPrivilegeId(AccountPrivilegeId accountPrivilegeId) {
        this.accountPrivilegeId = accountPrivilegeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_privilege_account_id"))
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountPrivilege)) return false;

        AccountPrivilege that = (AccountPrivilege) o;

        return getAccountPrivilegeId() != null ? getAccountPrivilegeId().equals(that
                .getAccountPrivilegeId()) : that.getAccountPrivilegeId() == null;
    }

    @Override
    public int hashCode() {
        return getAccountPrivilegeId() != null ? getAccountPrivilegeId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AccountPrivilege{" +
                "accountPrivilegeId=" + accountPrivilegeId +
                '}';
    }
}
