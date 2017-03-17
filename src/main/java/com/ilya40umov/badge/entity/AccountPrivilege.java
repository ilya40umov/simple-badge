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

    public static AccountPrivilege fromPrivilege(Privilege privilege) {
        return new AccountPrivilege()
                .setAccountPrivilegeId(new AccountPrivilegeId().setPrivilege(privilege));
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "privilege",
                    column = @Column(name = "privilege_id", nullable = false))
    })
    public AccountPrivilegeId getAccountPrivilegeId() {
        return accountPrivilegeId;
    }

    public AccountPrivilege setAccountPrivilegeId(AccountPrivilegeId accountPrivilegeId) {
        this.accountPrivilegeId = accountPrivilegeId;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("accountId")
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_account_privilege_account_id"))
    public Account getAccount() {
        return account;
    }

    public AccountPrivilege setAccount(Account account) {
        this.account = account;
        return this;
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
