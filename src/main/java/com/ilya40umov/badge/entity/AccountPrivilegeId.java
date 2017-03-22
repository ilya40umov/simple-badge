package com.ilya40umov.badge.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Natural primary key for {@link AccountPrivilege}.
 *
 * @author isorokoumov
 */
@Embeddable
public class AccountPrivilegeId implements Serializable {

    private Privilege privilege;
    private Long accountId;

    public Privilege getPrivilege() {
        return privilege;
    }

    public AccountPrivilegeId setPrivilege(Privilege privilege) {
        this.privilege = privilege;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public AccountPrivilegeId setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountPrivilegeId)) return false;

        AccountPrivilegeId that = (AccountPrivilegeId) o;

        if (getPrivilege() != that.getPrivilege()) return false;
        return getAccountId() != null ? getAccountId().equals(that.getAccountId()) : that
                .getAccountId() == null;
    }

    @Override
    public int hashCode() {
        // not using accountId for hasCode() because it only gets assigned after persist()
        return getPrivilege() != null ? getPrivilege().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AccountPrivilegeId{" +
                "privilege=" + privilege +
                ", accountId=" + accountId +
                '}';
    }
}
